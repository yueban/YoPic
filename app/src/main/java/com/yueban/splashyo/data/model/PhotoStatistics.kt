package com.yueban.splashyo.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PhotoStatistics(
    @SerializedName("downloads") @Embedded(prefix = "downloads_")
    val downloads: PhotoStatisticsItem,
    @SerializedName("id") @PrimaryKey
    val id: String = "",
    @SerializedName("views") @Embedded(prefix = "views_")
    val views: PhotoStatisticsItem,
    @SerializedName("likes") @Embedded(prefix = "likes_")
    val likes: PhotoStatisticsItem
)