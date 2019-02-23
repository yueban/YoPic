package com.yueban.splashyo.util.di.module

import com.squareup.moshi.Moshi
import com.yueban.splashyo.data.model.UnSplashKeys
import com.yueban.splashyo.data.net.RxJava2CallAdapterFactory
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.util.API_BASE_URL
import com.yueban.splashyo.util.di.scope.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * @author yueban
 * @date 2019/1/31
 * @email fbzhh007@gmail.com
 */
@Module
class ApiServiceModule {
    @AppScope
    @Provides
    fun provideOkHttpClient(unSplashKeys: UnSplashKeys): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.HEADERS
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Client-ID ${unSplashKeys.access_key}")
                    .addHeader("Accept-Version", "v1")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @AppScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): UnSplashService {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UnSplashService::class.java)
    }
}