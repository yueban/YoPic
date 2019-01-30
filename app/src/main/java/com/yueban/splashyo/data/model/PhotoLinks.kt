package com.yueban.splashyo.data.model

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
    @Deprecated("use download_link instead")
    val download: String = "",
    @Json(name = "download_location") @ColumnInfo(name = "download_location")
    val download_location: String = ""
) : Parcelable