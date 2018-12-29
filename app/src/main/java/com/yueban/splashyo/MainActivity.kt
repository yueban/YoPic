package com.yueban.splashyo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.data.model.PhotoStatistics
import com.yueban.splashyo.data.model.UnSplashKeys
import com.yueban.splashyo.data.net.UnSplashService
import com.yueban.splashyo.util.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, UnSplashKeys.getInstance(this).toString())

        val photoCache = Injection.providePhotoCache(this)

        // test get photos
        photoCache.getPhotos().observe(this, Observer<List<Photo>> {
            Log.d(TAG, "photos:\n$it")
        })
        val service = UnSplashService.create(this)
        service.photos(1, 20).enqueue(object : Callback<List<Photo>> {
            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.e(TAG, "", t)
            }

            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                response.body()?.let { photos ->
                    photoCache.insertPhotos(photos)
                }
            }
        })

        // test get photoStatistics
        val photoIdForTest = "z3RQCiIEbi4"
        photoCache.getPhotoStatistics(photoIdForTest).observe(this, Observer<PhotoStatistics> {
            Log.d(TAG, "PhotoStatistics:\n$it")
        })
        service.photoStatistics(photoIdForTest).enqueue(object : Callback<PhotoStatistics> {
            override fun onFailure(call: Call<PhotoStatistics>, t: Throwable) {
                Log.e(TAG, "", t)
            }

            override fun onResponse(call: Call<PhotoStatistics>, response: Response<PhotoStatistics>) {
                response.body()?.let { photoStatistics ->
                    photoCache.insertPhotoStatistics(photoStatistics)
                }
            }
        })

        // test get Collection
        photoCache.getCollections().observe(this, Observer<List<PhotoCollection>> {
            Log.d(TAG, "collections:\n$it")
        })
        service.collections(1, 30).enqueue(object : Callback<List<PhotoCollection>> {
            override fun onFailure(call: Call<List<PhotoCollection>>, t: Throwable) {
                Log.e(TAG, "", t)
            }

            override fun onResponse(call: Call<List<PhotoCollection>>, response: Response<List<PhotoCollection>>) {
                response.body()?.let { collections ->
                    photoCache.insertCollections(collections)
                }
            }
        })
    }
}