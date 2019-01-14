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
import timber.log.Timber

/**
 * @author yueban
 * @date 2019/1/5
 * @email fbzhh007@gmail.com
 */
class CollectionVM(private val photoRepo: PhotoRepo) : ViewModel() {
    private val nextPageHandler = NextPageHandler(photoRepo)
    private val _featured = MutableLiveData<Boolean>()
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

    val loadMoreStatus: LiveData<LoadMoreState>
        get() = nextPageHandler.loadMoreState

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
        private var nextPageLiveData: LiveData<Resource<List<PhotoCollection>>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var _hasMore: Boolean = false
        private var nextPage = 1
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun reset() {
            unregister()
            nextPage = 1
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                isRunning = false,
                errorMsg = null
            )
        }

        fun queryNextPage(featured: Boolean) {
            if (!_hasMore) {
                Timber.d("queryNextPage: no more")
                return
            }

            val state = loadMoreState.value
            if (state != null && state.isRunning) {
                Timber.d("queryNextPage: isRunning")
                return
            }
            unregister()
            nextPageLiveData = photoRepo.getCollections(featured, nextPage)
            loadMoreState.value = LoadMoreState(
                isRunning = true,
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
                    if (result.data == null) {
                        reset()
                        return
                    }
                    _hasMore = result.data.size >= PAGE_SIZE
                    unregister()
                    loadMoreState.value = LoadMoreState(
                        isRunning = false,
                        errorMsg = null
                    )
                    nextPage++
                }
                ERROR -> {
                    _hasMore = true
                    unregister()
                    loadMoreState.value = LoadMoreState(
                        isRunning = false,
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

    class LoadMoreState(val isRunning: Boolean, private val errorMsg: String?) {
        private var handledError = false

        val errorMsgIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMsg
            }
    }
}