package com.yueban.splashyo.util

import android.content.Context
import com.squareup.moshi.Moshi
import com.yueban.splashyo.data.local.db.AppDatabase
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.ui.detail.vm.PhotoDetailVMFactory
import com.yueban.splashyo.ui.main.vm.CollectionVMFactory
import com.yueban.splashyo.ui.main.vm.PhotoListVMFactory
import com.yueban.splashyo.util.moshi.ApplicationJsonAdapterFactory
import com.yueban.splashyo.util.moshi.MoshiDateConverter

/**
 * TODO(make members singleton by dagger2)
 *
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
object Injection {
    fun providePhotoDetailVMFactory(context: Context) = PhotoDetailVMFactory(providePhotoRepo(context))

    fun providePhotoListVMFactory(context: Context) = PhotoListVMFactory(providePhotoRepo(context))

    fun provideCollectionVMFactory(context: Context) = CollectionVMFactory(providePhotoRepo(context))

    fun providePhotoRepo(context: Context) =
        PhotoRepo(provideAppExecutors(), providePhotoDao(context), provideService(context))

    private fun providePhotoDao(context: Context) = provideDatabase(context).photoDao()

    private fun provideDatabase(context: Context) = AppDatabase.getInstance(context)

    private fun provideService(context: Context) = UnSplashService.create(context)

    fun provideAppExecutors() = AppExecutors.getInstance()

    fun provideMoshi() = Moshi.Builder()
        .add(MoshiDateConverter())
        .add(ApplicationJsonAdapterFactory.INSTANCE)
        .build()
}