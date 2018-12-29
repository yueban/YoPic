package com.yueban.splashyo.data.net

import android.content.Context
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics
import com.yueban.splashyo.data.model.UnSplashKeys
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
interface UnSplashService {
    @GET("photos/")
    fun photos(@Query("page") page: Int, @Query("per_page") per_page: Int): Call<List<Photo>>

    /**
     * @param photoId
     * @param resolution The frequency of the stats. (Optional; default: “days”)
     * @param quantity The amount of for each stat. (Optional; default: 30)
     */
    @GET("photos/{id}/statistics")
    fun photoStatistics(@Path("id") photoId: String, @Query("resolution") resolution: String = "days", @Query("quantity") quantity: Int = 30): Call<PhotoStatistics>

    @GET("collections/")
    fun collections(@Query("page") page: Int, @Query("per_page") per_page: Int): Call<List<PhotoCollection>>

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"

        fun create(context: Context): UnSplashService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val unSplashKeys = UnSplashKeys.getInstance(context)
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addNetworkInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Client-ID ${unSplashKeys.access_key}")
                        .addHeader("Accept-Version", "v1")
                        .build()
                    chain.proceed(request)
                }
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UnSplashService::class.java)
        }
    }
}