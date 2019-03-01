package com.yueban.splashyo.util.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.util.WallpaperSwitchOption
import com.yueban.splashyo.data.model.util.WallpaperSwitchOptionObservable

object WallpaperBindingAdapters {
    @JvmStatic
    @BindingAdapter("wallpaperSource")
    fun wallpaperSource(view: TextView, option: WallpaperSwitchOptionObservable) {
        view.text = when (option.sourceType) {
            WallpaperSwitchOption.SourceType.ALL_PHOTOS -> view.resources.getString(R.string.all_photos)
            WallpaperSwitchOption.SourceType.COLLECTION -> option.collectionName
            else -> ""
        }
    }

    @JvmStatic
    @BindingAdapter("wallpaperPeriod")
    fun wallpaperPeriod(view: TextView, period: Int) {
        view.text = when (period) {
            WallpaperSwitchOption.Period.MINUTE_30 -> view.resources.getString(R.string.thirty_minutes)
            WallpaperSwitchOption.Period.HOUR_1 -> view.resources.getString(R.string.one_hour)
            WallpaperSwitchOption.Period.HOUR_3 -> view.resources.getString(R.string.three_hours)
            WallpaperSwitchOption.Period.HOUR_12 -> view.resources.getString(R.string.twelve_hours)
            WallpaperSwitchOption.Period.HOUR_24 -> view.resources.getString(R.string.twenty_four_hours)
            else -> ""
        }
    }

    @JvmStatic
    @BindingAdapter("wallpaperSetType")
    fun wallpaperSetType(view: TextView, setType: Int) {
        view.text = when (setType) {
            WallpaperSwitchOption.SetType.HOME_SCREEN -> view.resources.getString(R.string.home_screen)
            WallpaperSwitchOption.SetType.LOCK_SCREEN -> view.resources.getString(R.string.lock_screen)
            WallpaperSwitchOption.SetType.BOTH -> view.resources.getString(R.string.home_screen_and_lock_screen)
            else -> ""
        }
    }
}