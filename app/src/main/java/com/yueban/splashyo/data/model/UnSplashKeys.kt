package com.yueban.splashyo.data.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.yueban.splashyo.util.UNSPLASH_KEYS_FILENAME

/**
 * @author yueban
 * @date 2018/12/24
 * @email fbzhh007@gmail.com
 */
data class UnSplashKeys(val access_key: String, val secret_key: String) {
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
            val jsonReader = JsonReader(inputStream.reader())
            return Gson().fromJson(jsonReader, UnSplashKeys::class.java)
        }
    }
}