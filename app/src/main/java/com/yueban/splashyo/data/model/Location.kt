package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    @SerializedName("country") @ColumnInfo(name = "country")
    val country: String = "",
    @SerializedName("city") @ColumnInfo(name = "city")
    val city: String? = null,
    @SerializedName("name") @ColumnInfo(name = "name")
    val name: String = "",
    @SerializedName("position") @Embedded(prefix = "position_")
    val position: Position,
    @SerializedName("title") @ColumnInfo(name = "title")
    val title: String = ""
) : Parcelable