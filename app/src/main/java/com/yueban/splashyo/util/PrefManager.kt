package com.yueban.splashyo.util

import android.content.Context
import androidx.annotation.IntDef
import com.yueban.splashyo.util.di.scope.AppScope
import net.grandcentrix.tray.TrayPreferences
import javax.inject.Inject

@AppScope
class PrefManager
@Inject constructor(
    context: Context
) : TrayPreferences(context, MODULE_NAME, VERSION) {

    companion object {
        private const val MODULE_NAME = "splashyo"
        private const val VERSION = 1
    }
}

object PrefKey {
    object Wallpaper {
        const val SOURCE_TYPE = "wallpaper_switch_source_type"
        const val SOURCE_ID = "wallpaper_switch_source_id"
        const val PERIOD = "wallpaper_switch_period"
        const val SET_TYPE = "wallpaper_switch_set_type"
        const val ONLY_IN_WIFI = "wallpaper_switch_only_in_wifi"
        const val CURRENT_ID = "wallpaper_switch_current_id"
    }
}

object PrefValue {
    object Wallpaper {
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
}

