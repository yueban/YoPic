package com.yueban.splashyo.ui.detail

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.databinding.ActivityPhotoDetailBinding

/**
 * @author yueban
 * @date 2019/1/17
 * @email fbzhh007@gmail.com
 */
class PhotoDetailActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPhotoDetailBinding
    private lateinit var mPhoto: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        if (extras == null) {
            Toast.makeText(this, R.string.photo_do_not_exist, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        mPhoto = PhotoDetailActivityArgs.fromBundle(extras).photo

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail)
        mBinding.photoImage.updateLayoutParams {
            height = Resources.getSystem().displayMetrics.widthPixels * mPhoto.height / mPhoto.width
        }
        mBinding.photo = mPhoto

        // TODO( get photo detail for VM (observe cache, refresh from api))
    }
}