package com.yueban.yopic.ui.base

import com.yueban.yopic.App
import com.yueban.yopic.util.di.component.AppComponent
import dagger.android.support.DaggerAppCompatActivity

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
abstract class BaseActivity : DaggerAppCompatActivity() {

    val appComponent: AppComponent
        get() = (application as App).appComponent
}