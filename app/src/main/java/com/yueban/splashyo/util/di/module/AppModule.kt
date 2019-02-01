package com.yueban.splashyo.util.di.module

import android.content.Context
import com.yueban.splashyo.SplashYoApp
import com.yueban.splashyo.util.di.scope.AppScope
import dagger.Module
import dagger.Provides

/**
 * @author yueban
 * @date 2019/1/31
 * @email fbzhh007@gmail.com
 */
@Module
class AppModule {
    @AppScope
    @Provides
    fun provideApplicationContext(splashYoApp: SplashYoApp): Context = splashYoApp.applicationContext
}