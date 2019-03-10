package com.yueban.yopic.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * @author yueban
 * @date 2019-03-10
 * @email fbzhh007@gmail.com
 */
/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/09/29
 * desc  : utils about toast
</pre> *
 */
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