package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Exif(
    @SerializedName("exposure_time") @ColumnInfo(name = "exposure_time")
    val exposureTime: String = "",
    @SerializedName("aperture") @ColumnInfo(name = "aperture")
    val aperture: String = "",
    @SerializedName("focal_length") @ColumnInfo(name = "focal_length")
    val focalLength: String = "",
    @SerializedName("iso") @ColumnInfo(name = "iso")
    val iso: Int = 0,
    @SerializedName("model") @ColumnInfo(name = "model")
    val model: String = "",
    @SerializedName("make") @ColumnInfo(name = "make")
    val make: String = ""
) : Parcelable