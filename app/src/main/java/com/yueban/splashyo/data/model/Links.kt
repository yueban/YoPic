package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links(
    @SerializedName("followers") @ColumnInfo(name = "followers")
    val followers: String? = "",
    @SerializedName("portfolio") @ColumnInfo(name = "portfolio")
    val portfolio: String? = "",
    @SerializedName("following") @ColumnInfo(name = "following")
    val following: String? = "",
    @SerializedName("self") @ColumnInfo(name = "self")
    val self: String = "",
    @SerializedName("html") @ColumnInfo(name = "html")
    val html: String = "",
    @SerializedName("photos") @ColumnInfo(name = "photos")
    val photos: String? = "",
    @SerializedName("likes") @ColumnInfo(name = "likes")
    val likes: String? = ""
) : Parcelable