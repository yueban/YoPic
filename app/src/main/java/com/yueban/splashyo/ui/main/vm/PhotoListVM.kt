package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.data.repo.model.Status.CACHE
import com.yueban.splashyo.data.repo.model.Status.ERROR
import com.yueban.splashyo.data.repo.model.Status.LOADING
import com.yueban.splashyo.data.repo.model.Status.SUCCESS
import com.yueban.splashyo.util.NullLiveData
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.vm.LoadState
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
    private val nextPageHandler = NextPageHandler(photoRepo)

    val cacheLabel: LiveData<String> = _cacheLabel
    val photos: LiveData<List<Photo>> = Transformations.switchMap(_cacheLabel) { it ->
        if (it == null) {
            NullLiveData.create()
        } else {
            photoRepo.getPhotosFromCache(it).also {
                nextPageHandler.reset()
                loadNextPage()
            }
        }
    }

    val loadStatus: LiveData<LoadState>
        get() = nextPageHandler.loadState

    val hasMore: Boolean
        get() = nextPageHandler.hasMore

    fun setCacheLabel(cacheLabel: String) {
        if (_cacheLabel.value == cacheLabel) {
            return
        }
        _cacheLabel.value = cacheLabel
    }

    fun refresh() {
        _cacheLabel.value?.let {
            _cacheLabel.value = _cacheLabel.value
        }
    }

    fun loadNextPage() {
        _cacheLabel.value?.let {
            nextPageHandler.queryNextPage(it)
        }
    }

    class NextPageHandler(private val photoRepo: PhotoRepo) : Observer<Resource<List<Photo>>> {
        var loadState = MutableLiveData<LoadState>()
        val hasMore: Boolean
            get() = _hasMore

        private val firstPage = 1
        private var nextPageLiveData: LiveData<Resource<List<Photo>>>? = null
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
            nextPageLiveData = photoRepo.getPhotos(cacheLabel, nextPage)
            loadState.value = LoadState(
                isRefreshing = nextPage == firstPage,
                isLoadingMore = nextPage != firstPage,
                errorMsg = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<List<Photo>>?) {
            if (result == null) {
                reset()
                return
            }

            when (result.status) {
                SUCCESS -> {
                    _hasMore =
                        if (result.data == null) {
                            false
                        } else {
                            result.data.size >= PAGE_SIZE
                        }
                    unregister()

                    loadState.value =
                        LoadState(
                            isRefreshing = false,
                            isLoadingMore = false,
                            errorMsg = null
                        )
                    nextPage++
                }
                CACHE -> {
                }
                ERROR -> {
                    _hasMore = true
                    unregister()
                    loadState.value = LoadState(
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMsg = result.message
                    )
                }
                LOADING -> {
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
        }
    }
}