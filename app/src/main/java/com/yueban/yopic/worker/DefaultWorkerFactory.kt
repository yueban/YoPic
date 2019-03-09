package com.yueban.yopic.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.elvishew.xlog.XLog
import com.yueban.yopic.data.net.UnSplashService
import com.yueban.yopic.util.PrefManager
import com.yueban.yopic.util.di.scope.AppScope
import javax.inject.Inject

/**
 * @author yueban
 * @date 2019-02-15
 * @email fbzhh007@gmail.com
 */
@AppScope
class DefaultWorkerFactory
@Inject constructor(
    private val service: UnSplashService,
    private val prefManager: PrefManager
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(workerClassName).asSubclass(ListenableWorker::class.java)
        } catch (var8: ClassNotFoundException) {
            XLog.e("$TAG: Class not found: $workerClassName", Exception())
            return null
        }

        return try {
            val constructor = clazz.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
            val worker = constructor.newInstance(appContext, workerParameters) as ListenableWorker
            when (worker) {
                is ChangeWallpaperWorker -> {
                    worker.service = service
                    worker.prefManager = prefManager
                }
            }
            worker
        } catch (e: Exception) {
            XLog.e("$TAG: Could not instantiate $workerClassName", e)
            null
        }
    }

    companion object {
        private const val TAG = "DefaultWorkerFactory"
    }
}