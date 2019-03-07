package com.yueban.yopic.util

import android.content.Context

object FileUtils {
    fun getLogFileFolder(context: Context): String {
        return getExternalCacheFolder(context)
    }

    private fun getExternalCacheFolder(context: Context): String {
        val dir = context.externalCacheDir
        return dir?.absolutePath ?: dir?.parentFile?.absolutePath ?: ""
    }
}
