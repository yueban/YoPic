package com.yueban.yopic.ui.main.di

import com.yueban.yopic.ui.main.CollectionFragment
import com.yueban.yopic.ui.main.PhotoListFragment
import com.yueban.yopic.util.di.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
@Module
abstract class MainActivityFragmentBindModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun collectionFragment(): CollectionFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun photoListFragment(): PhotoListFragment
}