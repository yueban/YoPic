package com.yueban.splashyo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.yueban.splashyo.databinding.ActivityMainBinding
import com.yueban.splashyo.ui.main.vm.CollectionVM
import com.yueban.splashyo.util.Injection
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

//        val photoRepo = Injection.providePhotoRepo(this)

//        photoRepo.getPhotos().observe(this, Observer { res ->
//            when (res.status) {
//                Status.SUCCESS -> {
//                    Timber.d("get photo list: success")
//                    res.data?.get(0)?.id?.let {
//                        getPhotoStat(photoRepo, it)
//                    }
//                }
//                Status.ERROR -> Timber.e("get photo list: error ${res.message}")
//                Status.LOADING -> Timber.d("get photo list: loading")
//            }
//        })
//
//        photoRepo.getCollections(true, 1).observe(this, Observer { res ->
//            when (res.status) {
//                Status.SUCCESS -> Timber.d("get collection list: success")
//                Status.ERROR -> Timber.e("get collection list: error ${res.message}")
//                Status.LOADING -> Timber.d("get collection list: loading")
//            }
//        })

        val collectionVM = Injection.provideCollectionVMFactory(this).create(CollectionVM::class.java)

        mBinding.loadNext.setOnClickListener {
            collectionVM.loadNextPage()
        }
        mBinding.switchFeatured.setOnClickListener {
            val featured = collectionVM.featured.value
            featured?.let { value ->
                collectionVM.setFeatured(!value)
            }
        }

        collectionVM.collections.observe(this, Observer {
            Timber.d("collections count: ${it.size}")
        })
        collectionVM.loadMoreStatus.observe(this, Observer {
            Timber.d("loadMore status: ${it.isRunning} ${it.errorMsg}")
        })
        collectionVM.setFeatured(true)
    }

//    private fun getPhotoStat(photoRepo: PhotoRepo, photoId: String) {
//        photoRepo.getPhotoStat(photoId).observe(this, Observer { res ->
//            when (res.status) {
//                Status.SUCCESS -> Timber.d("get photostat: success")
//                Status.ERROR -> Timber.e("get photostat: error ${res.message}")
//                Status.LOADING -> Timber.d("get photostat: loading")
//            }
//        })
//    }
}