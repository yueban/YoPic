package com.yueban.splashyo.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("total_photos") @ColumnInfo(name = "total_photos")
    val totalPhotos: Int = 0,
    @SerializedName("accepted_tos") @ColumnInfo(name = "accepted_tos")
    val acceptedTos: Boolean = false,
    @SerializedName("twitter_username") @ColumnInfo(name = "twitter_username")
    val twitterUsername: String? = "",
    @SerializedName("last_name") @ColumnInfo(name = "last_name")
    val lastName: String? = "",
    @SerializedName("bio") @ColumnInfo(name = "bio")
    val bio: String? = "",
    @SerializedName("total_likes") @ColumnInfo(name = "total_likes")
    val totalLikes: Int = 0,
    @SerializedName("portfolio_url") @ColumnInfo(name = "portfolio_url")
    val portfolioUrl: String? = "",
    @SerializedName("profile_image") @Embedded(prefix = "profile_image_")
    val profileImage: ProfileImage,
    @SerializedName("updated_at") @ColumnInfo(name = "updated_at")
    val updatedAt: String = "",
    @SerializedName("name") @ColumnInfo(name = "name")
    val name: String = "",
    @SerializedName("location") @ColumnInfo(name = "location")
    val location: String? = "",
    @SerializedName("links") @Embedded(prefix = "link_")
    val links: Links,
    @SerializedName("total_collections") @ColumnInfo(name = "total_collections")
    val totalCollections: Int = 0,
    @SerializedName("id") @ColumnInfo(name = "id")
    val id: String = "",
    @SerializedName("first_name") @ColumnInfo(name = "first_name")
    val firstName: String? = "",
    @SerializedName("instagram_username") @ColumnInfo(name = "instagram_username")
    val instagramUsername: String? = "",
    @SerializedName("username") @ColumnInfo(name = "username")
    val username: String = ""
)