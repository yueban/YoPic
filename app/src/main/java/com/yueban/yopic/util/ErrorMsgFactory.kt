package com.yueban.yopic.util

import android.content.Context
import com.elvishew.xlog.XLog
import com.yueban.yopic.R
import retrofit2.HttpException

/**
 * @author yueban
 * @date 2019-03-10
 * @email fbzhh007@gmail.com
 */
object ErrorMsgFactory {
    fun msg(context: Context, tr: Throwable?): String {
        XLog.e("ErrorMsgFactory - ${tr?.javaClass?.simpleName}", tr)
        return when (tr) {
            is HttpException -> context.getString(R.string.err_network)
            else -> {
                val errMsg = tr?.message ?: DefaultErrorMsg.get(context)
                if (tr != null) {
                    "${tr.javaClass.simpleName}: $errMsg"
                } else {
                    errMsg
                }
            }
        }
    }

    private object DefaultErrorMsg {
        @Volatile
        private var INSTANCE: String? = null

        fun get(context: Context): String {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: initDefaultErrorMsg(context).also { INSTANCE = it }
            }
        }

        private fun initDefaultErrorMsg(context: Context): String = context.getString(R.string.err_unknown)
    }
}