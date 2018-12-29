package com.yueban.splashyo.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class PhotoStatisticsItem(
    @SerializedName("total") @ColumnInfo(name = "total")
    val total: Int = 0,
    @SerializedName("historical") @Embedded(prefix = "historical_")
    val historical: Historical
)