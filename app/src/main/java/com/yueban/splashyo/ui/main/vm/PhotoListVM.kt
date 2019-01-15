package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.data.repo.model.Status
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
    private val _refreshTrigger = MutableLiveData<Any>()
    private val nextPageHandler = NextPageHandler(photoRepo)
    val photos: LiveData<List<Photo>> = Transformations.switchMap(_refreshTrigger) { it ->
        if (it == null) {
            NullLiveData.create()
        } else {
            photoRepo.getPhotosFromCache().also {
                nextPageHandler.reset()
                loadNextPage()
            }
        }
    }

    val loadStatus: LiveData<LoadState>
        get() = nextPageHandler.loadState

    fun refresh() {
        _refreshTrigger.value = {
            _refreshTrigger.value ?: Any()
        }
    }

    fun loadNextPage() {
        _refreshTrigger.value?.let {
            nextPageHandler.queryNextPage()
        }
    }

    class NextPageHandler(private val photoRepo: PhotoRepo) : Observer<Resource<List<Photo>>> {
        var loadState = MutableLiveData<LoadState>()

        private val firstPage = 1
        private var nextPageLiveData: LiveData<Resource<List<Photo>>>? = null
        private var hasMore: Boolean = false
        private var nextPage = firstPage

        init {
            reset()
        }

        fun reset() {
            unregister()
            nextPage = firstPage
            hasMore = true
            loadState.value = LoadState(
                isRefreshing = false,
                isLoadingMore = false,
                errorMsg = null
            )
        }

        fun queryNextPage() {
            if (!hasMore) {
                Timber.d("queryNextPage: no more")
                return
            }

            val state = loadState.value
            if (state != null && state.isRunning) {
                Timber.d("queryNextPage: isRunning")
                return
            }

            unregister()
            nextPageLiveData = photoRepo.getPhotos(nextPage)
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
                Status.SUCCESS -> {
                    hasMore =
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
                Status.ERROR -> {
                    hasMore = true
                    unregister()
                    loadState.value = LoadState(
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMsg = result.message
                    )
                }
                Status.LOADING -> {
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
        }
    }
}