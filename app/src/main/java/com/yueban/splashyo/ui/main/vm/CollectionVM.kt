package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.data.repo.model.Status.ERROR
import com.yueban.splashyo.data.repo.model.Status.LOADING
import com.yueban.splashyo.data.repo.model.Status.SUCCESS
import com.yueban.splashyo.util.NullLiveData
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.vm.LoadState
import timber.log.Timber

/**
 * @author yueban
 * @date 2019/1/5
 * @email fbzhh007@gmail.com
 */
class CollectionVM(private val photoRepo: PhotoRepo) : ViewModel() {
    private val nextPageHandler = NextPageHandler(photoRepo)
    private val _featured = MutableLiveData<Boolean>()

    init {
        _featured.value = true
    }

    val featured: LiveData<Boolean> = _featured

    // result
    val collections: LiveData<List<PhotoCollection>> = Transformations.switchMap(_featured) { featured ->
        if (featured == null) {
            NullLiveData.create()
        } else {
            photoRepo.getCollectionsFromCache(featured).also {
                nextPageHandler.reset()
                loadNextPage()
            }
        }
    }

    val loadStatus: LiveData<LoadState>
        get() = nextPageHandler.loadState

    val hasMore: Boolean
        get() = nextPageHandler.hasMore

    fun setFeatured(featured: Boolean) {
        if (_featured.value == featured) {
            return
        }
        _featured.value = featured
    }

    fun refresh() {
        _featured.value?.let {
            _featured.value = it
        }
    }

    fun loadNextPage() {
        _featured.value?.let {
            nextPageHandler.queryNextPage(it)
        }
    }

    class NextPageHandler(private val photoRepo: PhotoRepo) : Observer<Resource<List<PhotoCollection>>> {
        val loadState = MutableLiveData<LoadState>()
        val hasMore: Boolean
            get() = _hasMore

        private val firstPage = 1
        private var nextPageLiveData: LiveData<Resource<List<PhotoCollection>>>? = null
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
            nextPageLiveData = photoRepo.getCollections(featured, nextPage)
            loadState.value = LoadState(
                isRefreshing = nextPage == firstPage,
                isLoadingMore = nextPage != firstPage,
                errorMsg = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<List<PhotoCollection>>?) {
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
                    //ignore
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
        }
    }
}