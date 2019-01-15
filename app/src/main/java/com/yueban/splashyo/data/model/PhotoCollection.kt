package com.yueban.splashyo.data.model

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "collection")
data class PhotoCollection(
    @ColumnInfo(name = "rowid") @PrimaryKey(autoGenerate = true)
    val rowId: Int,
    @SerializedName("featured") @ColumnInfo(name = "featured")
    val featured: Boolean = false,
    @SerializedName("private") @ColumnInfo(name = "private")
    // private 是不可使用的关键字，这里使用 isPrivate 代替
    val isPrivate: Boolean = false,
    @SerializedName("cover_photo") @Embedded(prefix = "cover_photo_")
    val coverPhoto: Photo?,
    @SerializedName("total_photos") @ColumnInfo(name = "total_photos")
    val totalPhotos: Int = 0,
    @SerializedName("share_key") @ColumnInfo(name = "share_key")
    val shareKey: String? = "",
    @SerializedName("description") @ColumnInfo(name = "description")
    val description: String? = null,
    @SerializedName("title") @ColumnInfo(name = "title")
    val title: String = "",
    @SerializedName("tags") @ColumnInfo(name = "tags")
    val tags: List<TagItem>?,
    @SerializedName("preview_photos") @ColumnInfo(name = "preview_photos")
    val previewPhotos: List<PreviewPhoto>?,
    @SerializedName("updated_at") @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
    @SerializedName("curated") @ColumnInfo(name = "curated")
    val curated: Boolean = false,
    @SerializedName("links") @Embedded(prefix = "links_")
    val links: Links,
    @SerializedName("id") @ColumnInfo(name = "id")
    val id: Int = 0,
    @SerializedName("published_at") @ColumnInfo(name = "published_at")
    val publishedAt: Date,
    @SerializedName("user") @Embedded(prefix = "user_")
    val user: User?
) {
    val smallCoverImageUrl: String?
        get() = coverPhoto?.urls?.small

    val normalCoverImageUrl: String?
        get() = coverPhoto?.urls?.regular

    val originCoverImageUrl: String?
        get() = coverPhoto?.urls?.full

    val previewColor: Int?
        @SuppressLint("Range")
        get() = coverPhoto?.previewColor
}


