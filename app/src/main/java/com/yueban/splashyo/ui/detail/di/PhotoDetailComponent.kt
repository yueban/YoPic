package com.yueban.splashyo.ui.detail.di

import com.yueban.splashyo.ui.detail.PhotoDetailActivity
import com.yueban.splashyo.util.di.component.AppComponent
import com.yueban.splashyo.util.di.scope.ActivityScope
import dagger.Component

/**
 * @author yueban
 * @date 2019/1/31
 * @email fbzhh007@gmail.com
 */
@ActivityScope
@Component(dependencies = [AppComponent::class])
interface PhotoDetailComponent {
    fun inject(photoDetailActivity: PhotoDetailActivity)
}