package com.yueban.splashyo.ui.setting.di

import com.yueban.splashyo.ui.main.CollectionFragment
import com.yueban.splashyo.ui.setting.SettingFragment
import com.yueban.splashyo.util.di.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingActivityFragmentBindModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun settingFragment(): SettingFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun collectionFragmnet(): CollectionFragment
}