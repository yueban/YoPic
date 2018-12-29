package com.yueban.splashyo.data.local

import android.util.Log
import androidx.lifecycle.LiveData
import com.yueban.splashyo.data.local.db.PhotoDao
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics
import java.util.concurrent.Executor

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
class PhotoCache(
    private val photoDao: PhotoDao,
    private val ioExecutor: Executor
) {

    fun insertPhotos(photos: List<Photo>) {
        ioExecutor.execute {
            Log.d(TAG, "inserting ${photos.size} photos")
            photoDao.insertPhotos(photos)
        }
    }

    fun getPhotos(): LiveData<List<Photo>> {
        return photoDao.getPhotos()
    }

    fun insertPhotoStatistics(photoStatistics: PhotoStatistics) {
        ioExecutor.execute {
            Log.d(TAG, "inserting photo statistics, id: ${photoStatistics.id}")
            photoDao.insertPhotoStatistics(photoStatistics)
        }
    }

    fun getPhotoStatistics(photoId: String): LiveData<PhotoStatistics> {
        return photoDao.getPhotoStatistics(photoId)
    }

    fun insertCollections(collections: List<PhotoCollection>) {
        ioExecutor.execute {
            Log.d(TAG, "inserting ${collections.size} collections")
            photoDao.insertCollections(collections)
        }
    }

    fun getCollections(featured: Boolean = true): LiveData<List<PhotoCollection>> {
        return photoDao.getCollections(featured)
    }

    companion object {
        private const val TAG = "PhotoCache"
    }
}