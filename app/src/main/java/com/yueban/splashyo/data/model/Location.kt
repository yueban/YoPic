package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Location(
    @Json(name = "country") @ColumnInfo(name = "country")
    val country: String? = "",
    @Json(name = "city") @ColumnInfo(name = "city")
    val city: String? = null,
    @Json(name = "name") @ColumnInfo(name = "name")
    val name: String? = "",
    @Json(name = "position") @Embedded(prefix = "position_")
    val position: Position?,
    @Json(name = "title") @ColumnInfo(name = "title")
    val title: String? = ""
) : Parcelable