package com.yueban.splashyo.data.local

import androidx.lifecycle.LiveData
import com.yueban.splashyo.data.local.db.PhotoDao
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
class PhotoCache(
    private val photoDao: PhotoDao
) {
    fun insertPhotos(photos: List<Photo>) {
        photoDao.insertPhotos(photos)
    }

    fun getPhotos(): LiveData<List<Photo>> {
        return photoDao.getPhotos()
    }

    fun insertPhotoStatistics(photoStatistics: PhotoStatistics) {
        photoDao.insertPhotoStatistics(photoStatistics)
    }

    fun getPhotoStatistics(photoId: String): LiveData<PhotoStatistics> {
        return photoDao.getPhotoStatistics(photoId)
    }

    fun insertCollections(collections: List<PhotoCollection>) {
        photoDao.insertCollections(collections)
    }

    fun getCollections(featured: Boolean = true): LiveData<List<PhotoCollection>> {
        return photoDao.getCollections(featured)
    }

    companion object {
        private const val TAG = "PhotoCache"
    }
}