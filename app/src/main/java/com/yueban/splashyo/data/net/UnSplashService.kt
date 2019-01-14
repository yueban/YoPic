package com.yueban.splashyo.data.net

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.gson.GsonBuilder
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics
import com.yueban.splashyo.data.model.UnSplashKeys
import com.yueban.splashyo.util.PAGE_SIZE
import com.yueban.splashyo.util.UNSPLASH_DATE_FORMAT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    /**
     * @param order_by How to sort the photos. Optional. (Valid values: latest, oldest, popular; default: latest)
     */
    @GET("photos/")
    fun photos(@Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE, @Query("order_by") order_by: String = "latest"): LiveData<ApiResponse<List<Photo>>>

    /**
     * @param photoId
     * @param resolution The frequency of the stats. (Optional; default: “days”)
     * @param quantity The amount of for each stat. (Optional; default: 30)
     */
    @GET("photos/{id}/statistics")
    fun photoStatistics(@Path("id") photoId: String, @Query("resolution") resolution: String = "days", @Query("quantity") quantity: Int = 30): LiveData<ApiResponse<PhotoStatistics>>

    /**
     * order by [PhotoCollection.publishedAt]
     */
    @GET("collections")
    fun collections(@Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE): LiveData<ApiResponse<List<PhotoCollection>>>

    /**
     * order by [PhotoCollection.publishedAt]
     */
    @GET("collections/featured")
    fun collectionsFeatured(@Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE): LiveData<ApiResponse<List<PhotoCollection>>>

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
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat(UNSPLASH_DATE_FORMAT).create()))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(UnSplashService::class.java)
        }
    }
}