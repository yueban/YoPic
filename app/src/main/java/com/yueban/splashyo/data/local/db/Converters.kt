package com.yueban.splashyo.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yueban.splashyo.data.model.HistoricalValueItem
import com.yueban.splashyo.data.model.PreviewPhoto
import com.yueban.splashyo.data.model.TagItem
import java.util.Date

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
class Converters {
    private val gson: Gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun historicalValueItemListToString(data: List<HistoricalValueItem>?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToHistoricalValueItemList(data: String?): List<HistoricalValueItem>? {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<HistoricalValueItem>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun tagItemListToString(data: List<TagItem>?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToTagItemList(data: String?): List<TagItem>? {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<TagItem>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun previewPhotoListToString(data: List<PreviewPhoto>?): String? {
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToPreviewPhotoList(data: String?): List<PreviewPhoto>? {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<PreviewPhoto>>() {}.type
        return gson.fromJson(data, listType)
    }
}
