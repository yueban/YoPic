package com.yueban.yopic.util

import android.content.Context
import com.squareup.moshi.Moshi
import com.yueban.yopic.util.di.scope.AppScope
import net.grandcentrix.tray.TrayPreferences
import javax.inject.Inject

@AppScope
class PrefManager
@Inject constructor(
    context: Context,
    private val moshi: Moshi
) : TrayPreferences(context, MODULE_NAME, VERSION) {
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val data = getString(key, null)
        return if (data == null) {
            null
        } else {
            moshi.adapter(clazz).fromJson(data)
        }
    }

    fun <T> put(key: String, t: T, clazz: Class<T>) {
        val data = moshi.adapter(clazz).toJson(t)
        put(key, data)
    }

    companion object {
        private const val MODULE_NAME = "yopic"
        private const val VERSION = 1
    }
}

object PrefKey {
    const val WALLPAPER_SWITCH_OPTION = "wallpaper_switch_option"
}