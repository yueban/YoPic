package com.yueban.splashyo.util

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * @author yueban
 * @date 2019/1/30
 * @email fbzhh007@gmail.com
 */

val screenWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

val screenHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels

val displayMetrics: DisplayMetrics
    get() = Resources.getSystem().displayMetrics