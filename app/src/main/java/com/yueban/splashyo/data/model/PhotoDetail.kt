package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.Date

/**
 * see also [Photo]
 */
@Entity
@Parcelize
data class PhotoDetail(
    @SerializedName("sponsored_by") @Embedded(prefix = "sponsor_")
    val sponsoredBy: User?,
    @SerializedName("color") @ColumnInfo(name = "color")
    val color: String = "",
    @SerializedName("created_at") @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @SerializedName("description") @ColumnInfo(name = "description")
    val description: String? = "",
    @SerializedName("sponsored") @ColumnInfo(name = "sponsored")
    val sponsored: Boolean = false,
    @SerializedName("sponsored_impressions_id") @ColumnInfo(name = "sponsored_impressions_id")
    val sponsoredImpressionsId: String? = "",
    @SerializedName("liked_by_user") @ColumnInfo(name = "liked_by_user")
    val likedByUser: Boolean = false,
    @SerializedName("urls") @Embedded(prefix = "url_")
    val urls: Urls,
    @SerializedName("updated_at") @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
    @SerializedName("width") @ColumnInfo(name = "width")
    val width: Int = 0,
    @SerializedName("links") @Embedded(prefix = "link_")
    val links: Links,
    @SerializedName("id") @ColumnInfo(name = "id") @PrimaryKey
    val id: String = "",
    @SerializedName("user") @Embedded(prefix = "user_")
    val user: User,
    @SerializedName("height") @ColumnInfo(name = "height")
    val height: Int = 0,
    @SerializedName("likes") @ColumnInfo(name = "likes")
    val likes: Int = 0,
    @SerializedName("downloads") @ColumnInfo(name = "downloads")
    val downloads: Int = 0,
    @SerializedName("location") @Embedded(prefix = "location_")
    val location: Location,
    @SerializedName("slug") @ColumnInfo(name = "slug")
    val slug: String = "",
    @SerializedName("views") @ColumnInfo(name = "views")
    val views: Int = 0,
    @SerializedName("exif") @Embedded(prefix = "exif_")
    val exif: Exif
) : Parcelable
