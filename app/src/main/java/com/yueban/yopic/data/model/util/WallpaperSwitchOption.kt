package com.yueban.yopic.data.model.util

import android.os.Parcelable
import androidx.annotation.IntDef
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import se.ansman.kotshi.JsonSerializable

@JsonClass(generateAdapter = true)
@JsonSerializable
@Parcelize
data class WallpaperSwitchOption(
    val enabled: Boolean = false,
    @SourceType
    val sourceType: Int = SourceType.ALL_PHOTOS,
    val collectionId: String? = null,
    val collectionName: String? = null,
    @Period
    val period: Int = Period.HOUR_12,
    @SetType
    val setType: Int = SetType.HOME_SCREEN,
    val onlyInWifi: Boolean = false,
    val currentId: String? = null
) : Parcelable {
    val isEnabledAndValid: Boolean
        get() = enabled && period != 0

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SourceType.ALL_PHOTOS, SourceType.COLLECTION)
    annotation class SourceType {
        companion object {
            const val ALL_PHOTOS = 1
            const val COLLECTION = 2
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(Period.MINUTE_30, Period.HOUR_1, Period.HOUR_3, Period.HOUR_12, Period.HOUR_24)
    annotation class Period {
        companion object {
            const val MINUTE_30 = 30
            const val HOUR_1 = 60
            const val HOUR_3 = 60 * 3
            const val HOUR_12 = 60 * 12
            const val HOUR_24 = 60 * 24
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SetType.HOME_SCREEN, SetType.LOCK_SCREEN, SetType.BOTH)
    annotation class SetType {
        companion object {
            const val HOME_SCREEN = 1
            const val LOCK_SCREEN = 2
            const val BOTH = 3
        }
    }
}