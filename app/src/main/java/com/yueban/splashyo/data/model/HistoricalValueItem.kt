package com.yueban.splashyo.data.model

import com.google.gson.annotations.SerializedName

data class HistoricalValueItem(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("value")
    val value: Int = 0
)