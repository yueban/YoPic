package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PhotoLinks(
    @Json(name = "self") @ColumnInfo(name = "self")
    val self: String = "",
    @Json(name = "html") @ColumnInfo(name = "html")
    val html: String = "",
    @Json(name = "download") @ColumnInfo(name = "download")
    val download: String = "",
    /**
     * Unsplash 官方规定执行图片下载或类似操作时，需要请求一次 download_location 的链接以作统计
     */
    @Json(name = "download_location") @ColumnInfo(name = "download_location")
    val download_location: String = ""
) : Parcelable