package com.yueban.splashyo.util.ext

import android.content.Context
import com.yueban.splashyo.data.model.UnSplashKeys

/**
 * @author yueban
 * @date 2019/1/23
 * @email fbzhh007@gmail.com
 */
fun String.suffixWithUnSplashParams(context: Context): String = this + UnSplashKeys.getInstance(context).urlSuffix