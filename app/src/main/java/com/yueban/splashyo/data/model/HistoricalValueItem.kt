package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class HistoricalValueItem(
    @Json(name = "date") @ColumnInfo(name = "date")
    val date: String = "",
    @Json(name = "value") @ColumnInfo(name = "value")
    val value: Int = 0
) : Parcelable