package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Position(
    @Json(name = "latitude") @ColumnInfo(name = "latitude")
    val latitude: Double? = 0.0,
    @Json(name = "longitude") @ColumnInfo(name = "longitude")
    val longitude: Double? = 0.0
) : Parcelable