package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import java.util.Date

@JsonClass(generateAdapter = true)
@Parcelize
data class User(
    @Json(name = "total_photos") @ColumnInfo(name = "total_photos")
    val totalPhotos: Int = 0,
    @Json(name = "accepted_tos") @ColumnInfo(name = "accepted_tos")
    val acceptedTos: Boolean = false,
    @Json(name = "twitter_username") @ColumnInfo(name = "twitter_username")
    val twitterUsername: String? = "",
    @Json(name = "last_name") @ColumnInfo(name = "last_name")
    val lastName: String? = "",
    @Json(name = "bio") @ColumnInfo(name = "bio")
    val bio: String? = "",
    @Json(name = "total_likes") @ColumnInfo(name = "total_likes")
    val totalLikes: Int = 0,
    @Json(name = "portfolio_url") @ColumnInfo(name = "portfolio_url")
    val portfolioUrl: String? = "",
    @Json(name = "profile_image") @Embedded(prefix = "profile_image_")
    val profileImage: ProfileImage,
    @Json(name = "updated_at") @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
    @Json(name = "name") @ColumnInfo(name = "name")
    val name: String = "",
    @Json(name = "location") @ColumnInfo(name = "location")
    val location: String? = "",
    @Json(name = "links") @Embedded(prefix = "link_")
    val links: Links,
    @Json(name = "total_collections") @ColumnInfo(name = "total_collections")
    val totalCollections: Int = 0,
    @Json(name = "id") @ColumnInfo(name = "id")
    val id: String = "",
    @Json(name = "first_name") @ColumnInfo(name = "first_name")
    val firstName: String? = "",
    @Json(name = "instagram_username") @ColumnInfo(name = "instagram_username")
    val instagramUsername: String? = "",
    @Json(name = "username") @ColumnInfo(name = "username")
    val username: String = ""
) : Parcelable