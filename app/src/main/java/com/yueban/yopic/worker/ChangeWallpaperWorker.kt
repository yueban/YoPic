package com.yueban.yopic.worker

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.elvishew.xlog.XLog
import com.yueban.yopic.data.Optional
import com.yueban.yopic.data.model.Photo
import com.yueban.yopic.data.model.util.WallpaperSwitchOption
import com.yueban.yopic.data.net.UnSplashService
import com.yueban.yopic.util.GlideApp
import com.yueban.yopic.util.PrefKey
import com.yueban.yopic.util.PrefManager
import com.yueban.yopic.util.WallpaperUtil
import com.yueban.yopic.util.rxtransformer.AsyncScheduler
import com.yueban.yopic.util.screenHeight
import io.reactivex.Single
import kotlin.random.Random
import kotlin.random.nextInt

class ChangeWallpaperWorker(context: Context, params: WorkerParameters) : RxWorker(context, params) {
    lateinit var service: UnSplashService
    lateinit var prefManager: PrefManager

    override fun createWork(): Single<Result> {
        XLog.d("ChangeWallpaperWorker, create work")
        val option: WallpaperSwitchOption =
            prefManager.getObject(PrefKey.WALLPAPER_SWITCH_OPTION, WallpaperSwitchOption::class.java)
                ?: WallpaperSwitchOption()

        if (!option.isEnabledAndValid) {
            return Single.just(Result.failure())
        }

        val observable: Single<Optional<List<Photo>>> = when (option.sourceType) {
            WallpaperSwitchOption.SourceType.ALL_PHOTOS -> {
                service.photos(1)
            }
            WallpaperSwitchOption.SourceType.COLLECTION -> {
                if (option.collectionId.isNullOrEmpty()) {
                    Single.error(IllegalArgumentException("collectionId is null or empty"))
                } else {
                    service.photosByCollection(option.collectionId, 1)
                }
            }
            else -> {
                Single.error(IllegalArgumentException("sourceType value is illegal: ${option.sourceType}"))
            }
        }

        return observable.map {
            if (it.isNull || it.get().isEmpty()) {
                throw NullPointerException("photo list is null or empty")
            } else {
                val photos = it.get()
                // get a photo different from current wallpaper
                val photo: Photo
                if (photos.size == 1) {
                    photo = photos[0]
                } else {
                    while (true) {
                        val random = Random(System.currentTimeMillis())
                        val randomIndex = random.nextInt(0 until photos.size)
                        if (photos[randomIndex].id != option.currentId) {
                            photo = photos[randomIndex]
                            break
                        }
                    }
                }
                photo
            }
        }.flatMap { photo ->
            service.requestDownloadLocation(photo.links.download_location).map { photo }
        }.flatMap { photo ->
            val future = GlideApp.with(applicationContext).download(photo.resizeUrl(screenHeight)).submit()
            val file = future.get()
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            WallpaperUtil.setWallpaper(applicationContext, bitmap, option.setType)
            bitmap.recycle()
            XLog.d("ChangeWallpaperWorker, result success")
            Single.just(Result.success())
        }.onErrorReturn {
            XLog.e(it)
            if (it is NullPointerException) {
                XLog.d("ChangeWallpaperWorker, result failure")
                Result.failure()
            } else {
                XLog.d("ChangeWallpaperWorker, result retry")
                Result.retry()
            }
        }.compose(AsyncScheduler.create())
    }
}