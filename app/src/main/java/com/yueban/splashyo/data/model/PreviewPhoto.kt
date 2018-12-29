package com.yueban.splashyo.data.model

import com.google.gson.annotations.SerializedName

data class PreviewPhoto(
    @SerializedName("urls")
    val urls: Urls,
    @SerializedName("id")
    val id: String = ""
)