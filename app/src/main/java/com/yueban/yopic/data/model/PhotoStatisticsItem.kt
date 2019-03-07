package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PhotoStatisticsItem(
    @Json(name = "total") @ColumnInfo(name = "total")
    val total: Int = 0,
    @Json(name = "historical") @Embedded(prefix = "historical_")
    val historical: Historical
) : Parcelable