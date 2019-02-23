package com.yueban.splashyo.data.net

import androidx.lifecycle.LiveData
import com.yueban.splashyo.data.Optional
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoDetail
import com.yueban.splashyo.data.model.PhotoStatistics
import com.yueban.splashyo.util.PAGE_SIZE
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * @author yueban
 * @date 2018/12/25
 * @email fbzhh007@gmail.com
 */
interface UnSplashService {
    /**
     * @param order_by How to sort the photos. Optional. (Valid values: latest, oldest, popular; default: latest)
     */
    @GET("photos")
    fun photos(@Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE, @Query("order_by") order_by: String = "latest"): Single<Optional<List<Photo>>>

    @GET("/collections/{id}/photos")
    fun photosByCollection(@Path("id") collectionId: String, @Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE): Single<Optional<List<Photo>>>

    /**
     * @param photoId
     * @param resolution The frequency of the stats. (Optional; default: “days”)
     * @param quantity The amount of for each stat. (Optional; default: 30)
     */
    @GET("photos/{id}/statistics")
    fun photoStatistics(@Path("id") photoId: String, @Query("resolution") resolution: String = "days", @Query("quantity") quantity: Int = 30): LiveData<ApiResponse<PhotoStatistics>>

    @GET("photos/{id}")
    fun photoDetail(@Path("id") photoId: String): LiveData<ApiResponse<PhotoDetail>>

    @GET("collections")
    fun collections(@Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE): Single<Optional<List<PhotoCollection>>>

    @GET("collections/featured")
    fun collectionsFeatured(@Query("page") page: Int, @Query("per_page") per_page: Int = PAGE_SIZE): Single<Optional<List<PhotoCollection>>>

    /**
     * As it declared in UnSplash API guidelines: When your application performs something similar to a download (like when a user chooses the image to include in a blog post, set as a header, etc.), you must send a request to the download endpoint returned under the photo.links.download_location property.
     */
    @GET
    fun requestDownloadLocation(@Url download_location: String): LiveData<ApiResponse<Any>>
}