package com.yueban.yopic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class TagItem(
    @Json(name = "title") @ColumnInfo(name = "title")
    val title: String = ""
) : Parcelable