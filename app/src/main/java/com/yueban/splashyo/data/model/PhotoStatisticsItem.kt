package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoStatisticsItem(
    @SerializedName("total") @ColumnInfo(name = "total")
    val total: Int = 0,
    @SerializedName("historical") @Embedded(prefix = "historical_")
    val historical: Historical
) : Parcelable