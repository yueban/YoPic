package com.yueban.splashyo.worker

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.yueban.splashyo.data.Optional
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.util.GlideApp
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.PrefKey
import com.yueban.splashyo.util.PrefManager
import com.yueban.splashyo.util.PrefValue
import com.yueban.splashyo.util.WallpaperUtil
import com.yueban.splashyo.util.rxtransformer.AsyncScheduler
import com.yueban.splashyo.util.screenHeight
import io.reactivex.Single
import timber.log.Timber
import kotlin.random.Random
import kotlin.random.nextInt

class ChangeWallpaperWorker(context: Context, params: WorkerParameters) : RxWorker(context, params) {
    lateinit var service: UnSplashService
    lateinit var prefManager: PrefManager

    override fun createWork(): Single<Result> {
        Timber.d("executing work: changeWallpaper, photoRepo: $service")

        val sourceType = prefManager.getInt(PrefKey.Wallpaper.SOURCE_TYPE)
        val observable: Single<Optional<List<Photo>>>
        when (sourceType) {
            PrefValue.Wallpaper.SourceType.ALL_PHOTOS -> {
                observable = service.photos(1, PAGE_SIZE)
            }
            PrefValue.Wallpaper.SourceType.COLLECTION -> {
                val sourceId = prefManager.getString(PrefKey.Wallpaper.SOURCE_ID)
                observable = if (sourceId.isNullOrEmpty()) {
                    Single.error(IllegalArgumentException("collectionId is null or empty"))
                } else {
                    service.photosByCollection(sourceId, PAGE_SIZE)
                }
            }
            else -> {
                observable = Single.error(IllegalArgumentException("sourceType value is illegal: $sourceType"))
            }
        }

        return observable.map {
            if (it.isNull || it.get().isEmpty()) {
                throw NullPointerException("photo list is null or empty")
            } else {
                val photos = it.get()
                // get a photo different from current wallpaper
                val currentId = prefManager.getString(PrefKey.Wallpaper.CURRENT_ID)
                val photo: Photo
                if (photos.size == 1) {
                    photo = photos[0]
                } else {
                    while (true) {
                        val random = Random(System.currentTimeMillis())
                        val randomIndex = random.nextInt(0 until photos.size)
                        if (photos[randomIndex].id != currentId) {
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
            val setType = prefManager.getInt(PrefKey.Wallpaper.SET_TYPE)
            WallpaperUtil.setWallpaper(applicationContext, bitmap, setType)
            bitmap.recycle()
            Single.just(Result.success())
        }.onErrorReturn {
            Timber.e(it)
            Result.failure()
        }.compose(AsyncScheduler.create())
    }
}