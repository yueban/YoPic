package com.yueban.splashyo.ui.base

import androidx.fragment.app.Fragment
import com.yueban.splashyo.util.di.component.AppComponent

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
abstract class BaseFragment : Fragment() {

    val appComponent: AppComponent
        get() = (requireActivity() as BaseActivity).appComponent
}