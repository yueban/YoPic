package com.yueban.splashyo.worker

import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.elvishew.xlog.XLog
import com.yueban.splashyo.data.model.util.WallpaperSwitchOption
import com.yueban.splashyo.util.PrefKey
import com.yueban.splashyo.util.PrefManager
import com.yueban.splashyo.util.di.scope.AppScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author yueban
 * @date 2019-02-14
 * @email fbzhh007@gmail.com
 */
@AppScope
class WorkerUtil
@Inject constructor(
    context: Context,
    workerFactory: DefaultWorkerFactory,
    private val prefManager: PrefManager
) {
    init {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, config)
    }

    private val workManager = WorkManager.getInstance()

    /**
     * @param replace replace current work with a new one
     */
    fun refreshWallpaperChangeTask(replace: Boolean = false) {
        val option: WallpaperSwitchOption =
            prefManager.getObject(PrefKey.WALLPAPER_SWITCH_OPTION, WallpaperSwitchOption::class.java)
                ?: WallpaperSwitchOption()

        XLog.d("refreshWallpaperChangeTask start, replace: $replace\n$option")
        if (!option.isEnabledAndValid) {
            workManager.cancelUniqueWork(NAME_WALLPAPER_CHANGE)
            XLog.d("refreshWallpaperChangeTask start failed: option invalid, work canceled")
        } else {
            val constraints = if (option.onlyInWifi) {
                Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            } else {
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            }.build()

            val existingPolicy = if (replace) {
                ExistingPeriodicWorkPolicy.REPLACE
            } else {
                ExistingPeriodicWorkPolicy.KEEP
            }

            val wallpaperWorker =
                PeriodicWorkRequestBuilder<ChangeWallpaperWorker>(option.period.toLong(), TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()

            workManager.enqueueUniquePeriodicWork(
                NAME_WALLPAPER_CHANGE,
                existingPolicy,
                wallpaperWorker
            )
            XLog.d("refreshWallpaperChangeTask start success")
        }
    }

    companion object {
        private const val NAME_WALLPAPER_CHANGE = "NAME_WALLPAPER_CHANGE"
    }
}