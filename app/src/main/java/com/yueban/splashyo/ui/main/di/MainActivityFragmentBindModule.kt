package com.yueban.splashyo.ui.main.di

import com.yueban.splashyo.ui.main.CollectionFragment
import com.yueban.splashyo.ui.main.PhotoListFragment
import com.yueban.splashyo.util.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
@Module
abstract class MainActivityFragmentBindModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun collectionFragment(): CollectionFragment

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun photoListFragment(): PhotoListFragment
}