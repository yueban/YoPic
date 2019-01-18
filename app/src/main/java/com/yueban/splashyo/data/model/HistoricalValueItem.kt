package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoricalValueItem(
    @SerializedName("date") @ColumnInfo(name = "date")
    val date: String = "",
    @SerializedName("value") @ColumnInfo(name = "value")
    val value: Int = 0
) : Parcelable