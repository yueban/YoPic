package com.yueban.splashyo.util

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.yueban.splashyo.data.model.util.WallpaperSwitchOption

/**
 * @author yueban
 * @date 2019-02-14
 * @email fbzhh007@gmail.com
 */
object WallpaperUtil {
    fun setWallpaper(context: Context, bitmap: Bitmap, @WallpaperSwitchOption.SetType setType: Int) {
        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (setType) {
                WallpaperSwitchOption.SetType.HOME_SCREEN -> wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_SYSTEM
                )
                WallpaperSwitchOption.SetType.LOCK_SCREEN -> wallpaperManager.setBitmap(
                    bitmap,
                    null,
                    true,
                    WallpaperManager.FLAG_LOCK
                )
                WallpaperSwitchOption.SetType.BOTH -> wallpaperManager.setBitmap(
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