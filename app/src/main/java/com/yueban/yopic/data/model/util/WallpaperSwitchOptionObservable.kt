package com.yueban.yopic.data.model.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.yueban.yopic.BR

class WallpaperSwitchOptionObservable(option: WallpaperSwitchOption) : BaseObservable() {
    @get:Bindable
    var enabled: Boolean = option.enabled
        set(value) {
            field = value
            notifyPropertyChanged(BR.enabled)
        }

    @WallpaperSwitchOption.SourceType
    @get:Bindable
    var sourceType: Int = option.sourceType
        set(value) {
            field = value
            notifyChange()
        }
    var collectionId: String? = option.collectionId
    var collectionName: String? = option.collectionName

    @WallpaperSwitchOption.Period
    @get:Bindable
    var period: Int = option.period
        set(value) {
            field = value
            notifyPropertyChanged(BR.period)
        }

    @WallpaperSwitchOption.SetType
    @get:Bindable
    var setType: Int = option.setType
        set(value) {
            field = value
            notifyPropertyChanged(BR.setType)
        }

    var onlyInWifi: Boolean = option.onlyInWifi
    var currentId: String? = option.currentId

    fun toOption(): WallpaperSwitchOption = WallpaperSwitchOption(
        enabled,
        sourceType,
        collectionId,
        collectionName,
        period,
        setType,
        onlyInWifi,
        currentId
    )
}