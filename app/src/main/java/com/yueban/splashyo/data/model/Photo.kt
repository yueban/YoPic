package com.yueban.splashyo.data.model

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import se.ansman.kotshi.JsonDefaultValueLong
import se.ansman.kotshi.JsonSerializable
import java.util.Date

/**
 * see also [PhotoDetail]
 */
@Entity
@JsonClass(generateAdapter = true)
@JsonSerializable
@Parcelize
data class Photo(
    @JsonDefaultValueLong(0)
    @Json(name = "rowid")
    @ColumnInfo(name = "rowid")
    @PrimaryKey(autoGenerate = true)
    val rowId: Long,
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
    @Json(name = "id") @ColumnInfo(name = "id")
    val id: String = "",
    @Json(name = "user") @Embedded(prefix = "user_")
    val user: User,
    @Json(name = "height") @ColumnInfo(name = "height")
    val height: Int = 0,
    @Json(name = "likes") @ColumnInfo(name = "likes")
    val likes: Int = 0,
    /**
     * 缓存标记，用以区分不同场景下的缓存数据
     */
    @Json(name = "cache_label") @ColumnInfo(name = "cache_label")
    var cacheLabel: String? = ""
) : Parcelable {
    val userName: String
        get() = user.name

    val sponsorName: String
        get() {
            return if (sponsored && sponsoredBy != null) {
                sponsoredBy.name
            } else {
                ""
            }
        }

    val smallImageUrl: String
        get() = urls.small

    val normalImageUrl: String
        get() = urls.regular

    val originImageUrl: String
        get() = urls.full

    val previewColor: Int?
        @SuppressLint("Range")
        get() =
            if (color.isEmpty()) {
                null
            } else {
                Color.parseColor(color)
            }

    fun resizeUrl(height: Int): String {
        return "${urls.raw}&fm=jpg&h=$height&q=80&fit=max"
    }
}