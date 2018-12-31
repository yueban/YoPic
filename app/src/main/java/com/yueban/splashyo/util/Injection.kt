package com.yueban.splashyo.util

import android.content.Context
import com.yueban.splashyo.data.local.PhotoCache
import com.yueban.splashyo.data.local.db.AppDatabase
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.data.repo.PhotoRepo

/**
 * TODO(make members singleton by dagger2)
 *
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
object Injection {
    fun providePhotoRepo(context: Context) =
        PhotoRepo(provideAppExecutors(), providePhotoCache(context), provideService(context))

    private fun providePhotoCache(context: Context) = PhotoCache(providePhotoDao(context))

    private fun providePhotoDao(context: Context) = provideDatabase(context).photoDao()

    private fun provideDatabase(context: Context) = AppDatabase.getInstance(context)

    private fun provideService(context: Context) = UnSplashService.create(context)

    private fun provideAppExecutors() = AppExecutors.getInstance()
}