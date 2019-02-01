package com.yueban.splashyo.ui.base

import com.yueban.splashyo.ui.detail.PhotoDetailActivity
import com.yueban.splashyo.ui.main.MainActivity
import com.yueban.splashyo.ui.main.di.MainActivityFragmentBindModule
import com.yueban.splashyo.util.di.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
@Module
abstract class ActivityBindModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [MainActivityFragmentBindModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun photoDetailActivityInjector(): PhotoDetailActivity
}