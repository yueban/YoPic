package com.yueban.yopic.util.log

import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.flattener.DefaultFlattener
import com.yueban.yopic.util.TimeUtils

class LogFlattener : DefaultFlattener() {
    override fun flatten(timeMillis: Long, logLevel: Int, tag: String?, message: String?): CharSequence {
        return TimeUtils.millis2String(timeMillis) +
            '|'.toString() + LogLevel.getShortLevelName(logLevel) +
            '|'.toString() + tag + '|'.toString() +
            message
    }
}