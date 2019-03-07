package com.yueban.yopic.util.di.module

import android.content.Context
import com.yueban.yopic.App
import com.yueban.yopic.util.di.scope.AppScope
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
    fun provideApplicationContext(app: App): Context = app.applicationContext
}