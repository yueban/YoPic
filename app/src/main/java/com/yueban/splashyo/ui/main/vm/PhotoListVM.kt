package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.ext.orEmpty
import com.yueban.splashyo.util.rxtransformer.AsyncScheduler
import com.yueban.splashyo.util.rxtransformer.IgnoreErrorTransformer
import com.yueban.splashyo.util.vm.LoadState
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * @author yueban
 * @date 2019/1/15
 * @email fbzhh007@gmail.com
 */
class PhotoListVM(photoRepo: PhotoRepo) : ViewModel() {
    companion object {
        const val CACHE_LABEL_ALL = "all"
    }

    private val _cacheLabel = MutableLiveData<String>()
    private val _photos = MutableLiveData<List<Photo>>()
    private val nextPageHandler = NextPageHandler(_photos, photoRepo)

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
                LoadState.State.Idle,
                null
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
                        if (nextPage == firstPage) LoadState.State.Refreshing else LoadState.State.LoadingMore,
                        null
                    )
                }
                .doOnError {
                    _hasMore = true
                    loadState.value = LoadState(
                        LoadState.State.Error,
                        it.message
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

                    loadState.value =
                        LoadState(
                            LoadState.State.Success,
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