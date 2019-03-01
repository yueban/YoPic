package com.yueban.splashyo

import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.yueban.splashyo.util.di.component.AppComponent
import com.yueban.splashyo.util.di.component.BaseComponent
import com.yueban.splashyo.util.di.component.DaggerAppComponent
import com.yueban.splashyo.worker.WorkerUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * @author yueban
 * @date 2018/12/29
 * @email fbzhh007@gmail.com
 */
class SplashYoApp : DaggerApplication() {
    lateinit var appComponent: AppComponent
    @Inject
    lateinit var applicationDispatchingAndroidInjector: DispatchingAndroidInjector<SplashYoApp>
    @Inject
    lateinit var workerUtil: WorkerUtil

    override fun onCreate() {
        appComponent =
            DaggerAppComponent.builder()
                .baseComponent(BaseComponent.getInstance())
                .apply { seedInstance(this@SplashYoApp) }
                .build()
                .apply { inject(this@SplashYoApp) }

        super.onCreate()

        initTimber()
        initWorker()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initWorker() {
        workerUtil.refreshWallpaperChangeTask()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationDispatchingAndroidInjector

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
