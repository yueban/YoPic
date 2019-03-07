package com.yueban.yopic.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author yueban
 * @date 2018/12/24
 * @email fbzhh007@gmail.com
 */
@JsonClass(generateAdapter = true)
data class UnSplashKeys(
    @Json(name = "access_key")
    val access_key: String,
    @Json(name = "secret_key")
    val secret_key: String,
    @Json(name = "app_name")
    val app_name: String
) {
    /**
     * All links back to Unsplash should use utm parameters in the '?utm_source=your_app_name&utm_medium=referral'
     */
    val urlSuffix: String
        get() = "?utm_source=$app_name&utm_medium=referral"
}