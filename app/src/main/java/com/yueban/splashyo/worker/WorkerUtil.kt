package com.yueban.splashyo.worker

import android.content.Context
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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

    fun startWallpaperChangeTask(replace: Boolean = false) {
        val period = prefManager.getInt(PrefKey.Wallpaper.PERIOD, 0)
        if (period != 0) {
            val onlyInWifi = prefManager.getBoolean(PrefKey.Wallpaper.ONLY_IN_WIFI, false)

            val constraints = if (onlyInWifi) {
                Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            } else {
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            }.build()

            val wallpaperWorker = PeriodicWorkRequestBuilder<ChangeWallpaperWorker>(period.toLong(), TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            val existingPolicy = if (replace) {
                ExistingPeriodicWorkPolicy.REPLACE
            } else {
                ExistingPeriodicWorkPolicy.KEEP
            }

            workManager.enqueueUniquePeriodicWork(
                NAME_WALLPAPER_CHANGE,
                existingPolicy,
                wallpaperWorker
            )
        }
    }

    fun stopWallpaperChangeTask() {
        workManager.cancelUniqueWork(Companion.NAME_WALLPAPER_CHANGE)
    }

    companion object {
        private const val NAME_WALLPAPER_CHANGE = "NAME_WALLPAPER_CHANGE"
    }
}