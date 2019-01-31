package com.yueban.splashyo

import android.app.Application
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.yueban.splashyo.util.di.component.AppComponent
import com.yueban.splashyo.util.di.component.BaseComponent
import com.yueban.splashyo.util.di.component.DaggerAppComponent
import com.yueban.splashyo.util.di.module.AppModule
import timber.log.Timber

/**
 * @author yueban
 * @date 2018/12/29
 * @email fbzhh007@gmail.com
 */
class SplashYoApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent =
            DaggerAppComponent.builder()
                .baseComponent(BaseComponent.getInstance())
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setEnableHeaderTranslationContent(false)
                layout.setPrimaryColorsId(R.color.colorPrimary)//全局设置主题颜色
                MaterialHeader(context)
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                ClassicsFooter(context).setDrawableSize(20f).setFinishDuration(0)
            }
        }
    }
}