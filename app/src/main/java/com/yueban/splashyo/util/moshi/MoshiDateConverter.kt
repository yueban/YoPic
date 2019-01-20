package com.yueban.splashyo.util.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.yueban.splashyo.util.UNSPLASH_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author yueban
 * @date 2019/1/20
 * @email fbzhh007@gmail.com
 */
@Suppress("unused")
class MoshiDateConverter {
    @ToJson
    fun toJson(date: Date): String {
        return DATE_FORMATTER.format(date)
    }

    @FromJson
    fun fromJson(dateStr: String): Date {
        return DATE_FORMATTER.parse(dateStr)
    }

    companion object {
        val DATE_FORMATTER = SimpleDateFormat(UNSPLASH_DATE_FORMAT, Locale.US)
    }
}