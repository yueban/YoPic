package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Links(
    @Json(name = "followers") @ColumnInfo(name = "followers")
    val followers: String? = "",
    @Json(name = "portfolio") @ColumnInfo(name = "portfolio")
    val portfolio: String? = "",
    @Json(name = "following") @ColumnInfo(name = "following")
    val following: String? = "",
    @Json(name = "self") @ColumnInfo(name = "self")
    val self: String = "",
    @Json(name = "html") @ColumnInfo(name = "html")
    val html: String = "",
    @Json(name = "photos") @ColumnInfo(name = "photos")
    val photos: String? = "",
    @Json(name = "likes") @ColumnInfo(name = "likes")
    val likes: String? = ""
) : Parcelable