package com.yueban.yopic.util.di.module

import android.content.Context
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.yueban.yopic.data.model.UnSplashKeys
import com.yueban.yopic.util.UNSPLASH_KEYS_FILENAME
import com.yueban.yopic.util.di.scope.AppScope
import dagger.Module
import dagger.Provides
import okio.Buffer

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
@Module
class ConfigModule {
    @AppScope
    @Provides
    fun provideUnSplashKeys(context: Context, moshi: Moshi): UnSplashKeys {
        val inputStream = context.assets.open(UNSPLASH_KEYS_FILENAME)
        val jsonReader = JsonReader.of(Buffer().readFrom(inputStream))
        val adapter = moshi.adapter<UnSplashKeys>(UnSplashKeys::class.java)
        return adapter.fromJson(jsonReader)!!
    }
}