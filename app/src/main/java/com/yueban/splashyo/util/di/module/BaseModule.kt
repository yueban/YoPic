package com.yueban.splashyo.util.di.module

import com.squareup.moshi.Moshi
import com.yueban.splashyo.util.moshi.ApplicationJsonAdapterFactory
import com.yueban.splashyo.util.moshi.MoshiDateConverter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
@Module
class BaseModule {
    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(MoshiDateConverter())
            .add(ApplicationJsonAdapterFactory.INSTANCE)
            .build()
    }
}