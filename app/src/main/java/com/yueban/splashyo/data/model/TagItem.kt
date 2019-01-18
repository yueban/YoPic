package com.yueban.splashyo.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagItem(
    @SerializedName("title") @ColumnInfo(name = "title")
    val title: String = ""
) : Parcelable