package com.yueban.yopic.ui.base

import com.yueban.yopic.util.di.component.AppComponent
import dagger.android.support.DaggerFragment

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
abstract class BaseFragment : DaggerFragment() {

    val appComponent: AppComponent
        get() = (requireActivity() as BaseActivity).appComponent
}