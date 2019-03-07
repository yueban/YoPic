package com.yueban.yopic.ui.base

import com.yueban.yopic.ui.detail.PhotoDetailActivity
import com.yueban.yopic.ui.main.MainActivity
import com.yueban.yopic.ui.main.di.MainActivityFragmentBindModule
import com.yueban.yopic.ui.setting.SettingActivity
import com.yueban.yopic.ui.setting.di.SettingActivityFragmentBindModule
import com.yueban.yopic.util.di.scope.ActivityScope
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