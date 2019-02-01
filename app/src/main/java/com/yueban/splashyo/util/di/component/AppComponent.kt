package com.yueban.splashyo.util.di.component

import com.yueban.splashyo.SplashYoApp
import com.yueban.splashyo.data.model.UnSplashKeys
import com.yueban.splashyo.ui.base.ActivityBindModule
import com.yueban.splashyo.util.di.module.ApiServiceModule
import com.yueban.splashyo.util.di.module.AppModule
import com.yueban.splashyo.util.di.module.ConfigModule
import com.yueban.splashyo.util.di.module.DataSourceModule
import com.yueban.splashyo.util.di.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * 这里继承 [BaseComponent] 是为了把 [BaseComponent] 方法签名继承过来，以在 [AppComponent] 中暴漏同样的方法供子 component 获取
 *
 * @author yueban
 * @date 2019/1/30
 * @email fbzhh007@gmail.com
 */
@AppScope
@Component(
    dependencies = [BaseComponent::class],
    modules = [
        AppModule::class,
        ApiServiceModule::class,
        DataSourceModule::class,
        ConfigModule::class,
        ActivityBindModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : BaseComponent, AndroidInjector<SplashYoApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SplashYoApp>() {
        abstract fun baseComponent(baseComponent: BaseComponent): Builder

        abstract override fun build(): AppComponent
    }

    fun unSplashKeys(): UnSplashKeys
}