package com.yueban.splashyo.ui.detail

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.data.repo.model.Status.CACHE
import com.yueban.splashyo.data.repo.model.Status.ERROR
import com.yueban.splashyo.data.repo.model.Status.LOADING
import com.yueban.splashyo.data.repo.model.Status.SUCCESS
import com.yueban.splashyo.databinding.ActivityPhotoDetailBinding
import com.yueban.splashyo.ui.detail.vm.PhotoDetailVM
import com.yueban.splashyo.util.DEFAULT_ERROR_MSG
import com.yueban.splashyo.util.GlideApp
import com.yueban.splashyo.util.Injection
import com.yueban.splashyo.util.ext.suffixWithUnSplashParams
import com.yueban.splashyo.util.screenHeight
import com.yueban.splashyo.util.screenWidth

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
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //set fab
        mBinding.fabShare.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(mPhoto.links.html.suffixWithUnSplashParams(this@PhotoDetailActivity))
                startActivity(this)
            }

            mBinding.fabMenu.collapse()
        }
        mBinding.fabDownload.setOnClickListener {
            RxPermissions(this).request(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).subscribe { granted ->
                if (granted) {
                    mVM.download(mPhoto.links.download_location)
                }
            }
            mBinding.fabMenu.collapse()
        }
        mBinding.fabSetAsBackground.setOnClickListener {
            mVM.requestWallpaper(mPhoto.links.download_location)
            mBinding.fabMenu.collapse()
        }

        mBinding.photoImage.updateLayoutParams {
            height = screenWidth * mPhoto.height / mPhoto.width
        }
        mBinding.photo = mPhoto

        observeLiveData()
        initData()
    }

    private fun observeLiveData() {
        mVM.photoDetail.observe(this, Observer { res ->
            if (res == null) {
                showGetDetailError()
                return@Observer
            }
            when (res.status) {
                SUCCESS, CACHE -> mBinding.photoDetail = res.data
                ERROR -> showGetDetailError(res.message)
                LOADING -> {
                }
            }
        })

        mVM.requestDownloadResult.observe(this, Observer { res ->
            if (res == null) {
                showDownloadError()
                return@Observer
            }
            when (res.status) {
                SUCCESS, CACHE -> {
                    val downloadManager: DownloadManager =
                        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val request = DownloadManager.Request(Uri.parse(mPhoto.originImageUrl))
                    request.setDestinationInExternalFilesDir(
                        this@PhotoDetailActivity,
                        Environment.DIRECTORY_PICTURES,
                        "${mPhoto.id}.jpg"
                    )
                    request.allowScanningByMediaScanner()
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setMimeType("image/jpeg")
                    downloadManager.enqueue(request)
                }
                ERROR -> showDownloadError(res.message)
                LOADING -> {
                }
            }
        })

        mVM.requestWallpaperResult.observe(this, Observer { res ->
            if (res == null) {
                showSetWallpaperError()
                return@Observer
            }
            when (res.status) {
                SUCCESS, CACHE -> {
                    Injection.provideAppExecutors().networkIO().execute {
                        Injection.provideAppExecutors().mainThread().execute {
                            Snackbar.make(
                                mBinding.root,
                                getString(R.string.downloading_fitted_wallpaper),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        val future = GlideApp.with(this).download(mPhoto.resizeUrl(screenHeight)).submit()
                        val file = future.get()
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(this)
                        wallpaperManager.setBitmap(bitmap)
                        bitmap.recycle()

                        Injection.provideAppExecutors().mainThread().execute {
                            Snackbar.make(
                                mBinding.root,
                                getString(R.string.set_wallpaper_success),
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
                ERROR -> showSetWallpaperError(res.message)
                LOADING -> {
                }
            }
        })
    }

    private fun initData() {
        mVM.setPhotoId(mPhoto.id)
    }

    private fun showGetDetailError(errorMsg: String? = DEFAULT_ERROR_MSG) {
        val msg = errorMsg ?: DEFAULT_ERROR_MSG
        Snackbar.make(mBinding.root, msg, Snackbar.LENGTH_SHORT).setAction(getString(R.string.retry)) {
            mVM.retryGetDetail()
        }.show()
    }

    private fun showDownloadError(errorMsg: String? = DEFAULT_ERROR_MSG) {
        val msg = errorMsg ?: DEFAULT_ERROR_MSG
        Snackbar.make(mBinding.root, msg, Snackbar.LENGTH_SHORT).setAction(getString(R.string.retry)) {
            mVM.retryDownload()
        }.show()
    }

    private fun showSetWallpaperError(errorMsg: String? = DEFAULT_ERROR_MSG) {
        val msg = errorMsg ?: DEFAULT_ERROR_MSG
        Snackbar.make(mBinding.root, msg, Snackbar.LENGTH_SHORT).setAction(getString(R.string.retry)) {
            mVM.retryRequestWallpaper()
        }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}