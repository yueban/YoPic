package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.ext.emptyListIfNull
import com.yueban.splashyo.util.rxtransformer.AsyncScheduler
import com.yueban.splashyo.util.vm.LoadState
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author yueban
 * @date 2019/1/15
 * @email fbzhh007@gmail.com
 */
class PhotoListVM(private val photoRepo: PhotoRepo) : ViewModel() {
    companion object {
        const val CACHE_LABEL_ALL = "all"
    }

    private val _cacheLabel = MutableLiveData<String>()
    private val _photos = MutableLiveData<List<Photo>>()
    private val nextPageHandler = NextPageHandler(_photos, photoRepo)

    val cacheLabel: LiveData<String> = _cacheLabel
    val photos: LiveData<List<Photo>> = _photos

    val loadStatus: LiveData<LoadState>
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
        private val photos: MutableLiveData<List<Photo>>,
        private val photoRepo: PhotoRepo
    ) {
        var loadState = MutableLiveData<LoadState>()
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
            loadState.value = LoadState(
                isRefreshing = false,
                isLoadingMore = false,
                errorMsg = null
            )
        }

        fun queryNextPage(cacheLabel: String) {
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
            nextPageDisposable = photoRepo.getPhotos(cacheLabel, nextPage)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    loadState.value = LoadState(
                        isRefreshing = nextPage == firstPage,
                        isLoadingMore = nextPage != firstPage,
                        errorMsg = null
                    )
                }
                .doOnError {
                    _hasMore = true
                    loadState.value = LoadState(
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMsg = it.message
                    )
                }
                .subscribe { result ->
                    if (result.isCache) {
                        photos.value = result.getNullable().emptyListIfNull()
                    } else {
                        if (nextPage == firstPage) {
                            photos.value = result.getNullable().emptyListIfNull()
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

                    loadState.value =
                        LoadState(
                            isRefreshing = false,
                            isLoadingMore = false,
                            errorMsg = null
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