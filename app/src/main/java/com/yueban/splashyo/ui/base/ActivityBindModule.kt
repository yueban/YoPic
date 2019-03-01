package com.yueban.splashyo.ui.base

import com.yueban.splashyo.ui.detail.PhotoDetailActivity
import com.yueban.splashyo.ui.main.MainActivity
import com.yueban.splashyo.ui.main.di.MainActivityFragmentBindModule
import com.yueban.splashyo.ui.setting.SettingActivity
import com.yueban.splashyo.ui.setting.di.SettingActivityFragmentBindModule
import com.yueban.splashyo.util.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
@Module
abstract class ActivityBindModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityFragmentBindModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun photoDetailActivityInjector(): PhotoDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SettingActivityFragmentBindModule::class])
    abstract fun settingActivityInjector(): SettingActivity
}