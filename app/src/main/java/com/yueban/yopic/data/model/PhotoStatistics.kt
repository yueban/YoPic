package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity
@JsonClass(generateAdapter = true)
@Parcelize
data class PhotoStatistics(
    @Json(name = "downloads") @Embedded(prefix = "downloads_")
    val downloads: PhotoStatisticsItem,
    @Json(name = "id") @PrimaryKey
    val id: String = "",
    @Json(name = "views") @Embedded(prefix = "views_")
    val views: PhotoStatisticsItem,
    @Json(name = "likes") @Embedded(prefix = "likes_")
    val likes: PhotoStatisticsItem
) : Parcelable