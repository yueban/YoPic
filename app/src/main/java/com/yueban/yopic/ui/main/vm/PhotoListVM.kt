package com.yueban.yopic.ui.main.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elvishew.xlog.XLog
import com.yueban.yopic.data.model.Photo
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
 * @date 2019/1/15
 * @email fbzhh007@gmail.com
 */
class PhotoListVM(app: Application, photoRepo: PhotoRepo) : AndroidViewModel(app) {
    companion object {
        const val CACHE_LABEL_ALL = "all"
    }

    private val _cacheLabel = MutableLiveData<String>()
    private val _photos = MutableLiveData<List<Photo>>()
    private val nextPageHandler = NextPageHandler(getApplication(), _photos, photoRepo)

    val cacheLabel: LiveData<String> = _cacheLabel
    val photos: LiveData<List<Photo>> = _photos

    val loadState: LiveData<LoadState>
        get() = nextPageHandler.loadState

    val hasMore: Boolean
        get() = nextPageHandler.hasMore

    fun setCacheLabel(cacheLabel: String) {
        if (_cacheLabel.value == cacheLabel) {
            return
        }
        _cacheLabel.value = cacheLabel
        refresh()
    }

    fun refresh() {
        _cacheLabel.value?.let {
            nextPageHandler.reset()
            nextPageHandler.queryNextPage(it)
        }
    }

    fun loadNextPage() {
        _cacheLabel.value?.let {
            nextPageHandler.queryNextPage(it)
        }
    }

    class NextPageHandler(
        private val context: Context,
        private val photos: MutableLiveData<List<Photo>>,
        private val photoRepo: PhotoRepo
    ) {
        var loadState = MutableLiveData<LoadState>()
        val hasMore: Boolean
            get() = _hasMore

        private var nextPageDisposable: Disposable? = null
        private val firstPage = 1
        private var _hasMore: Boolean = false
        private var isRequesting: Boolean = false
        private var nextPage = firstPage

        init {
            reset()
        }

        fun reset() {
            unregister()
            nextPage = firstPage
            _hasMore = true
            isRequesting = false
        }

        fun queryNextPage(cacheLabel: String) {
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
                photoRepo.getPhotos(cacheLabel, nextPage, loadCacheOnFirstPage = photos.value.isNullOrEmpty())
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
                            photos.value = result.getNullable().orEmpty()
                        } else {
                            if (nextPage == firstPage) {
                                photos.value = result.getNullable().orEmpty()
                            } else if (!result.isNull) {
                                photos.apply {
                                    value = value!!.plus(result.get())
                                }
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