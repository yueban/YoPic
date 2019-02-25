package com.yueban.splashyo.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build

/**
 * @author yueban
 * @date 2019-02-14
 * @email fbzhh007@gmail.com
 */
object WallpaperUtil {
    fun setWallpaper(context: Context, bitmap: Bitmap, @PrefValue.Wallpaper.SetType setType: Int) {
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (setType) {
                PrefValue.Wallpaper.SetType.HOME_SCREEN -> wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_SYSTEM
                )
                PrefValue.Wallpaper.SetType.LOCK_SCREEN -> wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_LOCK
                )
                PrefValue.Wallpaper.SetType.BOTH -> wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                )
            }
        } else {
            wallpaperManager.setBitmap(bitmap)
        }
    }
}