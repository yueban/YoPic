package com.yueban.splashyo.ui.detail

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.repo.model.Status.CACHE
import com.yueban.splashyo.data.repo.model.Status.ERROR
import com.yueban.splashyo.data.repo.model.Status.LOADING
import com.yueban.splashyo.data.repo.model.Status.SUCCESS
import com.yueban.splashyo.databinding.ActivityPhotoDetailBinding
import com.yueban.splashyo.ui.detail.vm.PhotoDetailVM
import com.yueban.splashyo.util.DEFAULT_ERROR_MSG
import com.yueban.splashyo.util.Injection

/**
 * @author yueban
 * @date 2019/1/17
 * @email fbzhh007@gmail.com
 */
class PhotoDetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPhotoDetailBinding
    private lateinit var mVM: PhotoDetailVM
    private lateinit var mPhoto: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // parse arguments
        val extras = intent.extras
        if (extras == null) {
            Toast.makeText(this, R.string.photo_do_not_exist, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        mPhoto = PhotoDetailActivityArgs.fromBundle(extras).photo
        mVM = ViewModelProviders.of(this, Injection.providePhotoDetailVMFactory(this)).get(PhotoDetailVM::class.java)

        //set view
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail)
        mBinding.photoImage.updateLayoutParams {
            height = Resources.getSystem().displayMetrics.widthPixels * mPhoto.height / mPhoto.width
        }
        mBinding.photo = mPhoto

        observeLiveData()
        initData()
    }

    private fun observeLiveData() {
        mVM.photoDetail.observe(this, Observer { res ->
            if (res == null) {
                showError()
                return@Observer
            }
            when (res.status) {
                SUCCESS, CACHE -> mBinding.photoDetail = res.data
                ERROR -> showError(res.message)
                LOADING -> {
                }
            }
        })
    }

    private fun initData() {
        mVM.setPhotoId(mPhoto.id)
    }

    private fun showError(errorMsg: String? = DEFAULT_ERROR_MSG) {
        val msg = errorMsg ?: DEFAULT_ERROR_MSG
        Snackbar.make(mBinding.root, msg, Snackbar.LENGTH_SHORT).setAction("retry") {
            mVM.retry()
        }.show()
    }
}