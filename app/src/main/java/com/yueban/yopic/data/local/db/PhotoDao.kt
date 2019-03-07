package com.yueban.yopic.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yueban.yopic.data.model.Photo
import com.yueban.yopic.data.model.PhotoCollection
import com.yueban.yopic.data.model.PhotoDetail
import com.yueban.yopic.data.model.PhotoStatistics
import io.reactivex.Single

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>)

    /**
     * @param cacheLabel 用以区分不同场景下的缓存数据
     */
    @Query("delete from photo where cache_label=:cacheLabel")
    fun deleteAllPhotos(cacheLabel: String)

    /**
     * @param cacheLabel 用以区分不同场景下的缓存数据
     */
    @Query("select * from photo where cache_label=:cacheLabel")
    fun getPhotos(cacheLabel: String): Single<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollections(collections: List<PhotoCollection>): List<Long>

    @Query("select * from collection")
    fun getCollections(): Single<List<PhotoCollection>>

    @Query("select * from collection where featured=1")
    fun getFeaturedCollections(): Single<List<PhotoCollection>>

    @Query("delete from collection")
    fun deleteAllCollections()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoStatistics(photoStatistics: PhotoStatistics)

    @Query("select * from PhotoStatistics where id=:photoId limit 1")
    fun getPhotoStatistics(photoId: String): Single<PhotoStatistics>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoDetail(photoDetail: PhotoDetail)

    @Query("select * from PhotoDetail where id=:photoId")
    fun getPhotoDetail(photoId: String): Single<PhotoDetail>
}
