package com.yueban.splashyo.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TagItem(
    @SerializedName("title")
    val title: String = ""
) : Parcelable