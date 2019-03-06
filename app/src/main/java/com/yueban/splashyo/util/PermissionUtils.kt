package com.yueban.splashyo.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionUtils {
    /**
     * Return whether *you* have been granted the permissions.
     *
     * @param permissions The permissions.
     * @return `true`: yes `false`: no
     */
    fun isGranted(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (!isGranted(context, permission)) {
                return false
            }
        }
        return true
    }

    private fun isGranted(context: Context, permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            permission
        )
    }
}