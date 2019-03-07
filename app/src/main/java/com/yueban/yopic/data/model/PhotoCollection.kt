package com.yueban.yopic.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomWarnings
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import se.ansman.kotshi.JsonDefaultValueLong
import se.ansman.kotshi.JsonSerializable
import java.util.Date

@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "collection")
@JsonClass(generateAdapter = true)
@JsonSerializable
@Parcelize
data class PhotoCollection(
    @JsonDefaultValueLong(0)
    @Json(name = "rowid")
    @ColumnInfo(name = "rowid")
    @PrimaryKey(autoGenerate = true)
    val rowId: Long,
    @Json(name = "featured") @ColumnInfo(name = "featured")
    val featured: Boolean = false,
    @Json(name = "private") @ColumnInfo(name = "private")
    // private 是不可使用的关键字，这里使用 isPrivate 代替
    val isPrivate: Boolean = false,
    @Json(name = "cover_photo") @Embedded(prefix = "cover_photo_")
    val coverPhoto: Photo?,
    @Json(name = "total_photos") @ColumnInfo(name = "total_photos")
    val totalPhotos: Int = 0,
    @Json(name = "share_key") @ColumnInfo(name = "share_key")
    val shareKey: String? = "",
    @Json(name = "description") @ColumnInfo(name = "description")
    val description: String? = null,
    @Json(name = "title") @ColumnInfo(name = "title")
    val title: String = "",
    @Json(name = "tags") @ColumnInfo(name = "tags")
    val tags: List<TagItem>?,
    @Json(name = "preview_photos") @ColumnInfo(name = "preview_photos")
    val previewPhotos: List<PreviewPhoto>?,
    @Json(name = "updated_at") @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
    @Json(name = "curated") @ColumnInfo(name = "curated")
    val curated: Boolean = false,
    @Json(name = "links") @Embedded(prefix = "links_")
    val links: PhotoLinks,
    @Json(name = "id") @ColumnInfo(name = "id")
    val id: Int = 0,
    @Json(name = "published_at") @ColumnInfo(name = "published_at")
    val publishedAt: Date,
    @Json(name = "user") @Embedded(prefix = "user_")
    val user: User?
) : Parcelable {
    val smallCoverImageUrl: String?
        get() = coverPhoto?.urls?.small

    val previewColor: Int?
        @SuppressLint("Range")
        get() = coverPhoto?.previewColor
}


