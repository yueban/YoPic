package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PreviewPhoto(
    @Json(name = "urls") @Embedded(prefix = "urls")
    val urls: Urls,
    @Json(name = "id") @ColumnInfo(name = "id")
    val id: String = ""
) : Parcelable