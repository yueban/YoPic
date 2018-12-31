package com.yueban.splashyo

import android.app.Application
import timber.log.Timber

/**
 * @author yueban
 * @date 2018/12/29
 * @email fbzhh007@gmail.com
 */
class SplashYoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}