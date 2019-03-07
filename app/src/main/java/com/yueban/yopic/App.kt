package com.yueban.yopic

import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yueban.yopic.util.FileUtils
import com.yueban.yopic.util.di.component.AppComponent
import com.yueban.yopic.util.di.component.BaseComponent
import com.yueban.yopic.util.di.component.DaggerAppComponent
import com.yueban.yopic.util.log.LogBorderFormatter
import com.yueban.yopic.util.log.LogFlattener
import com.yueban.yopic.util.ui.DefaultRefreshFooter
import com.yueban.yopic.worker.WorkerUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

/**
 * @author yueban
 * @date 2018/12/29
 * @email fbzhh007@gmail.com
 */
class App : DaggerApplication() {
    lateinit var appComponent: AppComponent
    @Inject
    lateinit var applicationDispatchingAndroidInjector: DispatchingAndroidInjector<App>
    @Inject
    lateinit var workerUtil: WorkerUtil

    override fun onCreate() {
        appComponent =
            DaggerAppComponent.builder()
                .baseComponent(BaseComponent.getInstance())
                .apply { seedInstance(this@App) }
                .build()
                .apply { inject(this@App) }

        super.onCreate()

        initXLog()
        initWorker()
    }

    private fun initXLog() {
        val config = LogConfiguration.Builder()
            .logLevel(
                if (BuildConfig.DEBUG)
                    LogLevel.ALL
                else
                    LogLevel.NONE
            )
            .tag(getString(R.string.app_name))
            .b()
            .borderFormatter(LogBorderFormatter())
            .build()

        val androidPrinter = AndroidPrinter()
        val logFileFolder = FileUtils.getLogFileFolder(this)
        if (!logFileFolder.isEmpty()) {
            val filePrinter = FilePrinter.Builder(logFileFolder)
                .fileNameGenerator(DateFileNameGenerator())
                .flattener(LogFlattener())
                .build()
            XLog.init(
                config,
                androidPrinter,
                filePrinter
            )
        } else {
            XLog.init(
                config,
                androidPrinter
            )
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
                DefaultRefreshFooter(context)
            }
        }
    }
}
