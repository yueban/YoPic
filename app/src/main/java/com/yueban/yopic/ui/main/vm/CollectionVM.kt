package com.yueban.yopic.ui.main.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elvishew.xlog.XLog
import com.yueban.yopic.data.model.PhotoCollection
import com.yueban.yopic.data.repo.PhotoRepo
import com.yueban.yopic.util.ErrorMsgFactory
import com.yueban.yopic.util.PAGE_SIZE
import com.yueban.yopic.util.ext.orEmpty
import com.yueban.yopic.util.rxtransformer.AsyncScheduler
import com.yueban.yopic.util.rxtransformer.IgnoreErrorTransformer
import com.yueban.yopic.util.vm.LoadState
import io.reactivex.disposables.Disposable

/**
 * @author yueban
 * @date 2019/1/5
 * @email fbzhh007@gmail.com
 */
class CollectionVM(app: Application, photoRepo: PhotoRepo) : AndroidViewModel(app) {
    private val _featured = MutableLiveData<Boolean>()
    private val _collections = MutableLiveData<List<PhotoCollection>>()
    private val nextPageHandler = NextPageHandler(app, _collections, photoRepo)

    val featured: LiveData<Boolean> = _featured
    val collections: LiveData<List<PhotoCollection>> = _collections

    val loadState: LiveData<LoadState>
        get() = nextPageHandler.loadState

    val hasMore: Boolean
        get() = nextPageHandler.hasMore

    init {
        setFeatured(true)
    }

    fun setFeatured(featured: Boolean) {
        if (_featured.value == featured) {
            return
        }
        _featured.value = featured
        refresh()
    }

    fun refresh() {
        _featured.value?.let {
            nextPageHandler.reset()
            nextPageHandler.queryNextPage(it)
        }
    }

    fun loadNextPage() {
        _featured.value?.let {
            nextPageHandler.queryNextPage(it)
        }
    }

    class NextPageHandler(
        private val context: Context,
        private val collections: MutableLiveData<List<PhotoCollection>>,
        private val photoRepo: PhotoRepo
    ) {
        val loadState = MutableLiveData<LoadState>()
        val hasMore: Boolean
            get() = _hasMore

        private var nextPageDisposable: Disposable? = null
        private val firstPage = 1
        private var _hasMore: Boolean = false
        private var isRequesting: Boolean = false
        private var nextPage = firstPage

        fun reset() {
            unregister()
            nextPage = firstPage
            _hasMore = true
            isRequesting = false
        }

        fun queryNextPage(featured: Boolean) {
            if (!_hasMore) {
                XLog.d("queryNextPage: no more")
                return
            }

            if (isRequesting) {
                XLog.d("queryNextPage: isRequesting")
                return
            }
            unregister()

            nextPageDisposable =
                photoRepo.getCollections(featured, nextPage, loadCacheOnFirstPage = collections.value.isNullOrEmpty())
                    .compose(AsyncScheduler.create())
                    .doOnSubscribe {
                        isRequesting = true
                        loadState.value = LoadState(
                            if (nextPage == firstPage) LoadState.UIState.Refreshing else LoadState.UIState.LoadingMore,
                            null
                        )
                    }
                    .doOnError {
                        _hasMore = true
                        isRequesting = false
                        loadState.value = LoadState(
                            LoadState.UIState.Error,
                            ErrorMsgFactory.msg(context, it)
                        )
                    }
                    .compose(IgnoreErrorTransformer.create())
                    .subscribe { result ->
                        if (result.isCache) {
                            collections.value = result.getNullable().orEmpty()
                        } else {
                            if (nextPage == firstPage) {
                                collections.value = result.getNullable().orEmpty()
                            } else if (!result.isNull) {
                                collections.apply {
                                    value = collections.value!!.plus(result.get())
                                }
                            }

                            _hasMore =
                                if (result.isNull) {
                                    false
                                } else {
                                    result.get().size >= PAGE_SIZE
                                }

                            isRequesting = false

                            loadState.value =
                                LoadState(
                                    LoadState.UIState.Success,
                                    null
                                )

                            nextPage++
                        }
                    }
        }

        private fun unregister() {
            nextPageDisposable?.apply {
                if (!isDisposed) {
                    dispose()
                }
                nextPageDisposable = null
            }
        }
    }
}