package com.yueban.splashyo.data.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Historical(
    @SerializedName("quantity") @ColumnInfo(name = "quantity")
    val quantity: Int = 0,
    @SerializedName("change") @ColumnInfo(name = "change")
    val change: Int = 0,
    @SerializedName("values") @ColumnInfo(name = "values")
    val values: List<HistoricalValueItem>?,
    @SerializedName("resolution") @ColumnInfo(name = "resolution")
    val resolution: String = ""
)