package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ProfileImage(
    @Json(name = "small") @ColumnInfo(name = "small")
    val small: String = "",
    @Json(name = "large") @ColumnInfo(name = "large")
    val large: String = "",
    @Json(name = "medium") @ColumnInfo(name = "medium")
    val medium: String = ""
) : Parcelable