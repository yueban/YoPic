package com.yueban.splashyo.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>)

    @Query("select * from photo")
    fun getPhotos(): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoStatistics(photoStatistics: PhotoStatistics)

    @Query("select * from photostatistics where id=:photoId limit 1")
    fun getPhotoStatistics(photoId: String): LiveData<PhotoStatistics>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollections(collections: List<PhotoCollection>)

    @Query("select * from collection")
    fun getCollections(): LiveData<List<PhotoCollection>>

    @Query("select * from collection where featured=1")
    fun getFeaturedCollections(): LiveData<List<PhotoCollection>>

    @Query("delete from collection")
    fun deleteAllCollections()
}