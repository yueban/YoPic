package com.yueban.splashyo.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoricalValueItem(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("value")
    val value: Int = 0
) : Parcelable