package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.ext.orEmpty
import com.yueban.splashyo.util.rxtransformer.AsyncScheduler
import com.yueban.splashyo.util.rxtransformer.IgnoreErrorTransformer
import com.yueban.splashyo.util.vm.ListLoadState
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author yueban
 * @date 2019/1/5
 * @email fbzhh007@gmail.com
 */
class CollectionVM(photoRepo: PhotoRepo) : ViewModel() {
    private val _featured = MutableLiveData<Boolean>()
    private val _collections = MutableLiveData<List<PhotoCollection>>()
    private val nextPageHandler = NextPageHandler(_collections, photoRepo)

    val featured: LiveData<Boolean> = _featured
    val collections: LiveData<List<PhotoCollection>> = _collections

    val loadStatus: LiveData<ListLoadState>
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
        private val collections: MutableLiveData<List<PhotoCollection>>,
        private val photoRepo: PhotoRepo
    ) {
        val loadState = MutableLiveData<ListLoadState>()
        val hasMore: Boolean
            get() = _hasMore

        private var nextPageDisposable: Disposable? = null
        private val firstPage = 1
        private var _hasMore: Boolean = false
        private var nextPage = firstPage

        init {
            reset()
        }

        fun reset() {
            unregister()
            nextPage = firstPage
            _hasMore = true
            loadState.value = ListLoadState(
                isRefreshing = false,
                isLoadingMore = false,
                errorMsg = null
            )
        }

        fun queryNextPage(featured: Boolean) {
            if (!_hasMore) {
                Timber.d("queryNextPage: no more")
                return
            }

            val state = loadState.value
            if (state != null && state.isRunning) {
                Timber.d("queryNextPage: isRunning")
                return
            }
            unregister()

            nextPageDisposable = photoRepo.getCollections(featured, nextPage)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    loadState.value = ListLoadState(
                        isRefreshing = nextPage == firstPage,
                        isLoadingMore = nextPage != firstPage,
                        errorMsg = null
                    )
                }
                .doOnError {
                    _hasMore = true
                    loadState.value = ListLoadState(
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMsg = it.message
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

                        loadState.value =
                            ListLoadState(
                                isRefreshing = false,
                                isLoadingMore = false,
                                errorMsg = null
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