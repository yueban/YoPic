package com.yueban.splashyo.ui.base

import com.yueban.splashyo.SplashYoApp
import com.yueban.splashyo.util.di.component.AppComponent
import dagger.android.support.DaggerAppCompatActivity

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    val appComponent: AppComponent
        get() = (application as SplashYoApp).appComponent
}