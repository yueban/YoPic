package com.yueban.splashyo.util

import android.content.Context
import com.yueban.splashyo.data.local.PhotoCache
import com.yueban.splashyo.data.local.db.AppDatabase
import java.util.concurrent.Executors

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
object Injection {
    fun providePhotoCache(context: Context): PhotoCache {
        return PhotoCache(AppDatabase.getInstance(context).photoDao(), Executors.newSingleThreadExecutor())
    }
}