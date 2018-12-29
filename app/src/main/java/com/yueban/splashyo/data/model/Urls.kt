package com.yueban.splashyo.data.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Urls(
    @SerializedName("small") @ColumnInfo(name = "small")
    val small: String = "",
    @SerializedName("thumb") @ColumnInfo(name = "thumb")
    val thumb: String = "",
    @SerializedName("raw") @ColumnInfo(name = "raw")
    val raw: String = "",
    @SerializedName("regular") @ColumnInfo(name = "regular")
    val regular: String = "",
    @SerializedName("full") @ColumnInfo(name = "full")
    val full: String = ""
)