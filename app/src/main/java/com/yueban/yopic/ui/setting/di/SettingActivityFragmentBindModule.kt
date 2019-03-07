package com.yueban.yopic.ui.setting.di

import com.yueban.yopic.ui.main.CollectionFragment
import com.yueban.yopic.ui.setting.SettingFragment
import com.yueban.yopic.util.di.scope.FragmentScope
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