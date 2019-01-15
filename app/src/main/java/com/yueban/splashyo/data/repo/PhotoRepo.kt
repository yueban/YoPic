package com.yueban.splashyo.data.repo

import androidx.lifecycle.LiveData
import com.yueban.splashyo.data.local.db.PhotoDao
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics
import com.yueban.splashyo.data.net.ApiResponse
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.data.repo.model.NetworkBoundResource
import com.yueban.splashyo.data.repo.model.Resource
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
        page: Int,
        clearCacheOnFirstPage: Boolean = true,
        firstPage: Int = 1
    ): LiveData<Resource<List<Photo>>> {
        return object : NetworkBoundResource<List<Photo>, List<Photo>>(appExecutors) {
            override fun saveCallResult(data: List<Photo>) {
                Timber.d("photo list from api: ${data.size}")
                if (clearCacheOnFirstPage && page == firstPage) {
                    photoDao.deleteAllPhotos()
                }
                photoDao.insertPhotos(data)
            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                Timber.d("photo list from db: ${data?.size}")
                return true
            }

            override fun loadFromCache(): LiveData<List<Photo>> = getPhotosFromCache()

            override fun createCall(): LiveData<ApiResponse<List<Photo>>> = service.photos(page)
        }.asLiveData()
    }

    fun getPhotosFromCache(): LiveData<List<Photo>> = photoDao.getPhotos()

    fun getPhotoStat(photoId: String): LiveData<Resource<PhotoStatistics>> {
        return object : NetworkBoundResource<PhotoStatistics, PhotoStatistics>(appExecutors) {
            override fun saveCallResult(data: PhotoStatistics) {
                Timber.d("photostat from api: $data")
                photoDao.insertPhotoStatistics(data)
            }

            override fun shouldFetch(data: PhotoStatistics?): Boolean {
                Timber.d("photostat from db: $data")
                return true
            }

            override fun loadFromCache(): LiveData<PhotoStatistics> = photoDao.getPhotoStatistics(photoId)

            override fun createCall(): LiveData<ApiResponse<PhotoStatistics>> = service.photoStatistics(photoId)
        }.asLiveData()
    }

    fun getCollections(
        featured: Boolean,
        page: Int,
        clearCacheOnFirstPage: Boolean = true,
        firstPage: Int = 1
    ): LiveData<Resource<List<PhotoCollection>>> {
        return object : NetworkBoundResource<List<PhotoCollection>, List<PhotoCollection>>(appExecutors) {
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
}