package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Exif(
    @Json(name = "exposure_time") @ColumnInfo(name = "exposure_time")
    val exposureTime: String? = "",
    @Json(name = "aperture") @ColumnInfo(name = "aperture")
    val aperture: String? = "",
    @Json(name = "focal_length") @ColumnInfo(name = "focal_length")
    val focalLength: String? = "",
    @Json(name = "iso") @ColumnInfo(name = "iso")
    val iso: Int? = 0,
    @Json(name = "model") @ColumnInfo(name = "model")
    val model: String? = "",
    @Json(name = "make") @ColumnInfo(name = "make")
    val make: String? = ""
) : Parcelable