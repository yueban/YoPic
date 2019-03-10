package com.yueban.yopic.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {
    private var toast: Toast? = null

    fun show(context: Context, @StringRes resId: Int) {
        show(context, resId, Toast.LENGTH_SHORT)
    }

    fun show(context: Context, text: String) {
        show(context, text, Toast.LENGTH_SHORT)
    }

    fun show(context: Context, @StringRes resId: Int, duration: Int) {
        show(context, context.getString(resId), duration)
    }

    fun show(context: Context, text: String, duration: Int) {
        toast?.apply { cancel() }
        toast = Toast.makeText(context, text, duration)
        toast?.show()
    }
}