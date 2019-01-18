package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreviewPhoto(
    @SerializedName("urls") @Embedded(prefix = "urls")
    val urls: Urls,
    @SerializedName("id") @ColumnInfo(name = "id")
    val id: String = ""
) : Parcelable