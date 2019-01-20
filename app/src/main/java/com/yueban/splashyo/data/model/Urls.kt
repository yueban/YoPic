package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Urls(
    @Json(name = "small") @ColumnInfo(name = "small")
    val small: String = "",
    @Json(name = "thumb") @ColumnInfo(name = "thumb")
    val thumb: String = "",
    @Json(name = "raw") @ColumnInfo(name = "raw")
    val raw: String = "",
    @Json(name = "regular") @ColumnInfo(name = "regular")
    val regular: String = "",
    @Json(name = "full") @ColumnInfo(name = "full")
    val full: String = ""
) : Parcelable