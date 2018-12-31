package com.yueban.splashyo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.data.repo.model.Status
import com.yueban.splashyo.util.Injection
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val photoRepo = Injection.providePhotoRepo(this)

        photoRepo.getPhotos().observe(this, Observer { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    Timber.d("get photo list: success")
                    res.data?.get(0)?.id?.let {
                        getPhotoStat(photoRepo, it)
                    }
                }
                Status.ERROR -> Timber.e("get photo list: error ${res.message}")
                Status.LOADING -> Timber.d("get photo list: loading")
            }
        })

        photoRepo.getCollections(true).observe(this, Observer { res ->
            when (res.status) {
                Status.SUCCESS -> Timber.d("get collection list: success")
                Status.ERROR -> Timber.e("get collection list: error ${res.message}")
                Status.LOADING -> Timber.d("get collection list: loading")
            }
        })
    }

    private fun getPhotoStat(photoRepo: PhotoRepo, photoId: String) {
        photoRepo.getPhotoStat(photoId).observe(this, Observer { res ->
            when (res.status) {
                Status.SUCCESS -> Timber.d("get photostat: success")
                Status.ERROR -> Timber.e("get photostat: error ${res.message}")
                Status.LOADING -> Timber.d("get photostat: loading")
            }
        })
    }
}