package com.yueban.yopic.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yueban.yopic.BuildConfig
import com.yueban.yopic.data.model.Photo
import com.yueban.yopic.data.model.PhotoCollection
import com.yueban.yopic.data.model.PhotoDetail
import com.yueban.yopic.data.model.PhotoStatistics

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