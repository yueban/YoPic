package com.yueban.splashyo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yueban.splashyo.BuildConfig
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoDetail
import com.yueban.splashyo.data.model.PhotoStatistics

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
@Database(
    entities = [
        Photo::class,
        PhotoStatistics::class,
        PhotoCollection::class,
        PhotoDetail::class
    ],
    version = 1,
    exportSchema = !BuildConfig.isDebug
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}