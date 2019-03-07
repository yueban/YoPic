package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Historical(
    @Json(name = "quantity") @ColumnInfo(name = "quantity")
    val quantity: Int = 0,
    @Json(name = "change") @ColumnInfo(name = "change")
    val change: Int = 0,
    @Json(name = "values") @ColumnInfo(name = "values")
    val values: List<HistoricalValueItem>?,
    @Json(name = "resolution") @ColumnInfo(name = "resolution")
    val resolution: String = ""
) : Parcelable