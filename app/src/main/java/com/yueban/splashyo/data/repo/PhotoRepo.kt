package com.yueban.splashyo.data.repo

import androidx.lifecycle.LiveData
import com.yueban.splashyo.data.Optional
import com.yueban.splashyo.data.local.db.PhotoDao
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoDetail
import com.yueban.splashyo.data.net.ApiResponse
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.data.repo.model.NetworkBoundResource
import com.yueban.splashyo.data.repo.model.NetworkResource
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.ui.main.vm.PhotoListVM
import com.yueban.splashyo.util.di.scope.AppScope
import com.yueban.splashyo.util.rxtransformer.MarkAsCacheTransformer
import com.yueban.splashyo.util.rxtransformer.RoomOptionalTransformer
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * @author yueban
 * @date 2018/12/30
 * @email fbzhh007@gmail.com
 */
@AppScope
class PhotoRepo
@Inject constructor(
    private val photoDao: PhotoDao,
    private val service: UnSplashService
) {
    fun getPhotoDetail(photoId: String): LiveData<Resource<PhotoDetail>> {
        return object : NetworkBoundResource<PhotoDetail>() {
            override fun saveCallResult(data: PhotoDetail) {
                photoDao.insertPhotoDetail(data)
            }

            override fun shouldFetch(data: PhotoDetail?): Boolean {
                // cache expired time: 1 minute
                data?.let {
                    val timeSpan = System.currentTimeMillis() - it.cacheUpdateAt
                    return timeSpan > 1000 * 60
                }
                return true
            }

            override fun preloadCache(data: PhotoDetail?): Boolean = data != null

            override fun loadFromCache(): LiveData<PhotoDetail> = photoDao.getPhotoDetail(photoId)

            override fun loadFromNet(): LiveData<ApiResponse<PhotoDetail>> = service.photoDetail(photoId)
        }.asLiveData()
    }

    fun requestDownloadLocation(downloadLocation: String): LiveData<Resource<Any>> {
        return object : NetworkResource<Any>() {
            override fun loadFromNet(): LiveData<ApiResponse<Any>> = service.requestDownloadLocation(downloadLocation)
        }.asLiveData()
    }

    fun getCollections(
        featured: Boolean,
        page: Int,
        clearCacheOnFirstPage: Boolean = true,
        firstPage: Int = 1
    ): Flowable<Optional<List<PhotoCollection>>> {
        val netSource =
            if (featured) {
                service.collectionsFeatured(page)
            } else {
                service.collections(page)
            }.doOnSuccess {
                if (!it.isNull) {
                    if (clearCacheOnFirstPage && page == firstPage) {
                        photoDao.deleteAllCollections()
                    }
                    photoDao.insertCollections(it.get())
                }
            }

        return if (page == firstPage) {
            if (featured) {
                photoDao.getFeaturedCollections()
            } else {
                photoDao.getCollections()
            }.compose(RoomOptionalTransformer())
                .compose(MarkAsCacheTransformer())
                .concatWith(netSource)
        } else {
            netSource.toFlowable()
        }
    }

    fun getPhotos(
        cacheLabel: String,
        page: Int,
        clearCacheOnFirstPage: Boolean = true,
        firstPage: Int = 1
    ): Flowable<Optional<List<Photo>>> {
        val netSource =
            if (cacheLabel == PhotoListVM.CACHE_LABEL_ALL) {
                service.photos(page)
            } else {
                service.photosByCollection(cacheLabel, page)
            }.doOnSuccess {
                if (!it.isNull) {
                    // add cache label
                    it.get().forEach { photo -> photo.cacheLabel = cacheLabel }
                    if (clearCacheOnFirstPage && page == firstPage) {
                        photoDao.deleteAllPhotos(cacheLabel)
                    }
                    photoDao.insertPhotos(it.get())
                }
            }

        return if (page == firstPage) {
            photoDao.getPhotos(cacheLabel)
                .compose(RoomOptionalTransformer())
                .compose(MarkAsCacheTransformer())
                .concatWith(netSource)
        } else {
            netSource.toFlowable()
        }
    }
}
