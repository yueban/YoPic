package com.yueban.splashyo.data.model

import android.content.Context
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonReader
import com.yueban.splashyo.util.Injection
import com.yueban.splashyo.util.UNSPLASH_KEYS_FILENAME
import okio.Buffer

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

    companion object {
        @Volatile
        private var instance: UnSplashKeys? = null

        fun getInstance(context: Context): UnSplashKeys {
            return instance ?: synchronized(this) {
                instance ?: getUnSplashKeys(context).also {
                    instance = it
                }
            }
        }

        private fun getUnSplashKeys(context: Context): UnSplashKeys {
            val inputStream = context.assets.open(UNSPLASH_KEYS_FILENAME)
            val jsonReader = JsonReader.of(Buffer().readFrom(inputStream))
            val adapter = Injection.provideMoshi().adapter<UnSplashKeys>(UnSplashKeys::class.java)
            return adapter.fromJson(jsonReader)!!
        }
    }
}