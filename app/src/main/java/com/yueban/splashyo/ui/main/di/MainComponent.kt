package com.yueban.splashyo.ui.main.di

import com.yueban.splashyo.ui.main.CollectionFragment
import com.yueban.splashyo.ui.main.PhotoListFragment
import com.yueban.splashyo.util.di.component.AppComponent
import com.yueban.splashyo.util.di.scope.ActivityScope
import dagger.Component

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
@ActivityScope
@Component(dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(collectionFragment: CollectionFragment)

    fun inject(photoListFragment: PhotoListFragment)
}