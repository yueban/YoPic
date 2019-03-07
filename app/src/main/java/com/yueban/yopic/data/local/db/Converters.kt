package com.yueban.yopic.data.local.db

import androidx.room.TypeConverter
import com.squareup.moshi.Types
import com.yueban.yopic.data.model.HistoricalValueItem
import com.yueban.yopic.data.model.PreviewPhoto
import com.yueban.yopic.data.model.TagItem
import com.yueban.yopic.util.di.component.BaseComponent
import java.util.Date

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
class Converters {
    private val moshi = BaseComponent.getInstance().moshi()

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
        val adapter = moshi.adapter<List<HistoricalValueItem>>(
            Types.newParameterizedType(
                List::class.java,
                HistoricalValueItem::class.java
            )
        )
        return adapter.toJson(data)
    }

    @TypeConverter
    fun stringToHistoricalValueItemList(data: String?): List<HistoricalValueItem>? {
        if (data == null) {
            return emptyList()
        }

        val adapter = moshi.adapter<List<HistoricalValueItem>>(
            Types.newParameterizedType(
                List::class.java,
                HistoricalValueItem::class.java
            )
        )
        return adapter.fromJsonValue(data)
    }

    @TypeConverter
    fun tagItemListToString(data: List<TagItem>?): String? {
        val adapter = moshi.adapter<List<TagItem>>(Types.newParameterizedType(List::class.java, TagItem::class.java))
        return adapter.toJson(data)
    }

    @TypeConverter
    fun stringToTagItemList(data: String?): List<TagItem>? {
        if (data == null) {
            return emptyList()
        }

        val adapter = moshi.adapter<List<TagItem>>(Types.newParameterizedType(List::class.java, TagItem::class.java))
        return adapter.fromJson(data)
    }

    @TypeConverter
    fun previewPhotoListToString(data: List<PreviewPhoto>?): String? {
        val adapter =
            moshi.adapter<List<PreviewPhoto>>(Types.newParameterizedType(List::class.java, PreviewPhoto::class.java))
        return adapter.toJson(data)
    }

    @TypeConverter
    fun stringToPreviewPhotoList(data: String?): List<PreviewPhoto>? {
        if (data == null) {
            return emptyList()
        }

        val adapter =
            moshi.adapter<List<PreviewPhoto>>(Types.newParameterizedType(List::class.java, PreviewPhoto::class.java))
        return adapter.fromJson(data)
    }
}
