package com.yueban.splashyo.util.log

import com.elvishew.xlog.formatter.border.DefaultBorderFormatter
import com.elvishew.xlog.internal.SystemCompat

class LogBorderFormatter : DefaultBorderFormatter() {
    override fun format(segments: Array<out String>?): String {
        var formatted = super.format(segments)
        if (formatted.isNotEmpty()) {
            formatted = " " + SystemCompat.lineSeparator + formatted
        }
        return formatted
    }
}