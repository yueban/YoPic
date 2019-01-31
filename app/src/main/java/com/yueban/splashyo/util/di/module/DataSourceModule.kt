package com.yueban.splashyo.util.di.module

import android.content.Context
import androidx.room.Room
import com.yueban.splashyo.data.local.db.AppDatabase
import com.yueban.splashyo.data.local.db.PhotoDao
import com.yueban.splashyo.util.APP_DATABASE_NAME
import com.yueban.splashyo.util.di.scope.AppScope
import dagger.Module
import dagger.Provides

/**
 * @author yueban
 * @date 2019/1/31
 * @email fbzhh007@gmail.com
 */
@Module
class DataSourceModule {
    @AppScope
    @Provides
    fun provideAppDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME).build()
    }

    @AppScope
    @Provides
    fun providePhotoDao(appDatabase: AppDatabase): PhotoDao = appDatabase.photoDao()
}