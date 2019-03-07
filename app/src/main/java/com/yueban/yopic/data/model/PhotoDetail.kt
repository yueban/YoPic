package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yueban.yopic.util.moshi.defaultvalue.MoshiDefaultCacheUpdateTime
import kotlinx.android.parcel.Parcelize
import se.ansman.kotshi.JsonSerializable
import java.util.Date

/**
 * see also [Photo]
 */
@Entity
@JsonClass(generateAdapter = true)
@JsonSerializable
@Parcelize
data class PhotoDetail(
    @Json(name = "sponsored_by") @Embedded(prefix = "sponsor_")
    val sponsoredBy: User?,
    @Json(name = "color") @ColumnInfo(name = "color")
    val color: String = "",
    @Json(name = "created_at") @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @Json(name = "description") @ColumnInfo(name = "description")
    val description: String? = "",
    @Json(name = "sponsored") @ColumnInfo(name = "sponsored")
    val sponsored: Boolean = false,
    @Json(name = "sponsored_impressions_id") @ColumnInfo(name = "sponsored_impressions_id")
    val sponsoredImpressionsId: String? = "",
    @Json(name = "liked_by_user") @ColumnInfo(name = "liked_by_user")
    val likedByUser: Boolean = false,
    @Json(name = "urls") @Embedded(prefix = "url_")
    val urls: Urls,
    @Json(name = "updated_at") @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
    @Json(name = "width") @ColumnInfo(name = "width")
    val width: Int = 0,
    @Json(name = "links") @Embedded(prefix = "link_")
    val links: PhotoLinks,
    @Json(name = "id") @ColumnInfo(name = "id") @PrimaryKey
    val id: String = "",
    @Json(name = "user") @Embedded(prefix = "user_")
    val user: User,
    @Json(name = "height") @ColumnInfo(name = "height")
    val height: Int = 0,
    @Json(name = "likes") @ColumnInfo(name = "likes")
    val likes: Int = 0,
    @Json(name = "downloads") @ColumnInfo(name = "downloads")
    val downloads: Int = 0,
    @Json(name = "location") @Embedded(prefix = "location_")
    val location: Location?,
    @Json(name = "slug") @ColumnInfo(name = "slug")
    val slug: String? = "",
    @Json(name = "views") @ColumnInfo(name = "views")
    val views: Int = 0,
    @Json(name = "exif") @Embedded(prefix = "exif_")
    val exif: Exif?,
    /**
     * 记录缓存更新时间
     */
    @Json(name = "cacheUpdateAt") @ColumnInfo(name = "cache_update_at")
    @MoshiDefaultCacheUpdateTime
    val cacheUpdateAt: Long = Date().time
) : Parcelable {
    val dimensions: String
        get() = "$width x $height"

    companion object {
        @MoshiDefaultCacheUpdateTime
        fun defaultUpdateTime() = Date().time
    }
}
