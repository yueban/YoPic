package com.yueban.splashyo.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.yueban.splashyo.SplashYoApp
import com.yueban.splashyo.util.di.component.AppComponent

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
abstract class BaseActivity : AppCompatActivity() {

    val appComponent: AppComponent
        get() = (application as SplashYoApp).appComponent
}