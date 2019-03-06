package com.yueban.splashyo.util

import androidx.annotation.NonNull
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("unused", "MemberVisibilityCanBePrivate")
object TimeUtils {
    private val SDF_THREAD_LOCAL = ThreadLocal<SimpleDateFormat>()

    private fun getDefaultFormat(): SimpleDateFormat {
        var simpleDateFormat = SDF_THREAD_LOCAL.get()
        if (simpleDateFormat == null) {
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            SDF_THREAD_LOCAL.set(simpleDateFormat)
        }
        return simpleDateFormat
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * The pattern is `yyyy-MM-dd HH:mm:ss`.
     *
     * @param millis The milliseconds.
     * @return the formatted time string
     */
    fun millis2String(millis: Long): String {
        return millis2String(millis, getDefaultFormat())
    }

    /**
     * Milliseconds to the formatted time string.
     *
     * @param millis The milliseconds.
     * @param format The format.
     * @return the formatted time string
     */
    fun millis2String(millis: Long, @NonNull format: DateFormat): String {
        return format.format(Date(millis))
    }

    /**
     * Formatted time string to the milliseconds.
     *
     * The pattern is `yyyy-MM-dd HH:mm:ss`.
     *
     * @param time The formatted time string.
     * @return the milliseconds
     */
    fun string2Millis(time: String): Long {
        return string2Millis(time, getDefaultFormat())
    }

    /**
     * Formatted time string to the milliseconds.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the milliseconds
     */
    fun string2Millis(time: String, @NonNull format: DateFormat): Long {
        try {
            return format.parse(time).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return -1
    }

    /**
     * Formatted time string to the date.
     *
     * The pattern is `yyyy-MM-dd HH:mm:ss`.
     *
     * @param time The formatted time string.
     * @return the date
     */
    fun string2Date(time: String): Date? {
        return string2Date(time, getDefaultFormat())
    }

    /**
     * Formatted time string to the date.
     *
     * @param time   The formatted time string.
     * @param format The format.
     * @return the date
     */
    fun string2Date(time: String, format: DateFormat): Date? {
        try {
            return format.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Date to the formatted time string.
     *
     * The pattern is `yyyy-MM-dd HH:mm:ss`.
     *
     * @param date The date.
     * @return the formatted time string
     */
    fun date2String(date: Date): String {
        return date2String(date, getDefaultFormat())
    }

    /**
     * Date to the formatted time string.
     *
     * @param date   The date.
     * @param format The format.
     * @return the formatted time string
     */
    fun date2String(date: Date, @NonNull format: DateFormat): String {
        return format.format(date)
    }

    /**
     * Date to the milliseconds.
     *
     * @param date The date.
     * @return the milliseconds
     */
    fun date2Millis(date: Date): Long {
        return date.time
    }

    /**
     * Milliseconds to the date.
     *
     * @param millis The milliseconds.
     * @return the date
     */
    fun millis2Date(millis: Long): Date {
        return Date(millis)
    }
}