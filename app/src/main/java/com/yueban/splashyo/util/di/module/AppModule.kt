package com.yueban.splashyo.util.di.module

import android.app.Application
import android.content.Context
import com.yueban.splashyo.util.di.scope.AppScope
import dagger.Module
import dagger.Provides

/**
 * @author yueban
 * @date 2019/1/31
 * @email fbzhh007@gmail.com
 */
@Module
class AppModule(val app: Application) {
    @AppScope
    @Provides
    fun provideApplication(): Application = app

    @AppScope
    @Provides
    fun provideApplicationContext(): Context = app
}