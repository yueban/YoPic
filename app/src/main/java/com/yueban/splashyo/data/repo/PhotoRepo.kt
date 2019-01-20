package com.yueban.splashyo.data.repo

import androidx.lifecycle.LiveData
import com.yueban.splashyo.data.local.db.PhotoDao
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoDetail
import com.yueban.splashyo.data.model.PhotoStatistics
import com.yueban.splashyo.data.net.ApiResponse
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.data.repo.model.NetworkBoundResource
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.ui.main.vm.PhotoListVM
import com.yueban.splashyo.util.AppExecutors
import timber.log.Timber

/**
 * @author yueban
 * @date 2018/12/30
 * @email fbzhh007@gmail.com
 */
class PhotoRepo(
    private val appExecutors: AppExecutors,
    private val photoDao: PhotoDao,
    private val service: UnSplashService
) {
    fun getPhotos(
        cacheLabel: String,
        page: Int,
        clearCacheOnFirstPage: Boolean = true,
        firstPage: Int = 1
    ): LiveData<Resource<List<Photo>>> {
        return object : NetworkBoundResource<List<Photo>>(appExecutors) {
            override fun processResponse(response: ApiResponse.ApiSuccessResponse<List<Photo>>): List<Photo> {
                response.body.forEach { it.cacheLabel = cacheLabel }
                return response.body
            }

            override fun saveCallResult(data: List<Photo>) {
                Timber.d("photo list from api: ${data.size}")
                if (clearCacheOnFirstPage && page == firstPage) {
                    photoDao.deleteAllPhotos(cacheLabel)
                }
                photoDao.insertPhotos(data)
            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                Timber.d("photo list from db: ${data?.size}")
                return true
            }

            override fun loadFromCache(): LiveData<List<Photo>> = getPhotosFromCache(cacheLabel)

            override fun createCall(): LiveData<ApiResponse<List<Photo>>> =
                if (cacheLabel == PhotoListVM.CACHE_LABEL_ALL) {
                    service.photos(page)
                } else {
                    service.photosByCollection(cacheLabel, page)
                }
        }.asLiveData()
    }

    fun getPhotosFromCache(cacheLabel: String): LiveData<List<Photo>> = photoDao.getPhotos(cacheLabel)

    fun getCollections(
        featured: Boolean,
        page: Int,
        clearCacheOnFirstPage: Boolean = true,
        firstPage: Int = 1
    ): LiveData<Resource<List<PhotoCollection>>> {
        return object : NetworkBoundResource<List<PhotoCollection>>(appExecutors) {
            override fun saveCallResult(data: List<PhotoCollection>) {
                Timber.d("collection list from api: ${data.size}")
                // clear cache on get first page
                if (clearCacheOnFirstPage && page == firstPage) {
                    photoDao.deleteAllCollections()
                }
                photoDao.insertCollections(data)
            }

            override fun shouldFetch(data: List<PhotoCollection>?): Boolean {
                Timber.d("collection list from db: ${data?.size}")
                return true
            }

            override fun loadFromCache(): LiveData<List<PhotoCollection>> = getCollectionsFromCache(featured)

            override fun createCall(): LiveData<ApiResponse<List<PhotoCollection>>> =
                if (featured) {
                    service.collectionsFeatured(page)
                } else {
                    service.collections(page)
                }
        }.asLiveData()
    }

    fun getCollectionsFromCache(featured: Boolean): LiveData<List<PhotoCollection>> =
        if (featured) {
            photoDao.getFeaturedCollections()
        } else {
            photoDao.getCollections()
        }

    fun getPhotoStat(photoId: String): LiveData<Resource<PhotoStatistics>> {
        return object : NetworkBoundResource<PhotoStatistics>(appExecutors) {
            override fun saveCallResult(data: PhotoStatistics) {
                Timber.d("photostat from api: $data")
                photoDao.insertPhotoStatistics(data)
            }

            override fun shouldFetch(data: PhotoStatistics?): Boolean {
                Timber.d("photostat from db: $data")
                return true
            }

            override fun resultFromCache(): Boolean {
                return true
            }

            override fun loadFromCache(): LiveData<PhotoStatistics> = photoDao.getPhotoStatistics(photoId)

            override fun createCall(): LiveData<ApiResponse<PhotoStatistics>> = service.photoStatistics(photoId)
        }.asLiveData()
    }

    fun getPhotoDetailFromCache(photoId: String): LiveData<PhotoDetail> = photoDao.getPhotoDetail(photoId)

    fun getPhotoDetail(photoId: String): LiveData<Resource<PhotoDetail>> {
        return object : NetworkBoundResource<PhotoDetail>(appExecutors) {
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

            override fun loadFromCache(): LiveData<PhotoDetail> = getPhotoDetailFromCache(photoId)

            override fun resultFromCache(): Boolean = true

            override fun skipCacheResultWhenFetchFromNet(data: PhotoDetail?): Boolean = data == null

            override fun createCall(): LiveData<ApiResponse<PhotoDetail>> = service.photoDetail(photoId)
        }.asLiveData()
    }
}
