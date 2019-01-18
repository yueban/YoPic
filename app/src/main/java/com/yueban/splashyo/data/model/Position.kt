package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Position(
    @SerializedName("latitude") @ColumnInfo(name = "latitude")
    val latitude: Double = 0.0,
    @SerializedName("longitude") @ColumnInfo(name = "longitude")
    val longitude: Double = 0.0
) : Parcelable