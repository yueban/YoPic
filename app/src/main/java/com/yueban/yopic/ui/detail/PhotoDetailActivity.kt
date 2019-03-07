package com.yueban.yopic.ui.detail

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment
import com.yueban.yopic.R
import com.yueban.yopic.data.model.Photo
import com.yueban.yopic.data.model.util.WallpaperSwitchOption
import com.yueban.yopic.data.repo.model.Status
import com.yueban.yopic.databinding.ActivityPhotoDetailBinding
import com.yueban.yopic.ui.base.BaseViewActivity
import com.yueban.yopic.ui.detail.vm.PhotoDetailVM
import com.yueban.yopic.ui.detail.vm.PhotoDetailVMFactory
import com.yueban.yopic.util.DEFAULT_ERROR_MSG
import com.yueban.yopic.util.GlideApp
import com.yueban.yopic.util.WallpaperUtil
import com.yueban.yopic.util.bottomsheet.SimpleBottomSheetListener
import com.yueban.yopic.util.concurrent.AppExecutors
import com.yueban.yopic.util.screenHeight
import com.yueban.yopic.util.screenWidth
import javax.inject.Inject

/**
 * @author yueban
 * @date 2019/1/17
 * @email fbzhh007@gmail.com
 */
class PhotoDetailActivity : BaseViewActivity<ActivityPhotoDetailBinding>() {
    private lateinit var mVM: PhotoDetailVM

    private lateinit var mPhoto: Photo
    @Inject
    lateinit var photoDetailVMFactory: PhotoDetailVMFactory

    override fun getLayoutId(): Int = R.layout.activity_photo_detail

    override fun initVMAndParams(savedInstanceState: Bundle?): Boolean {
        mVM = ViewModelProviders.of(this, photoDetailVMFactory).get(PhotoDetailVM::class.java)

        return try {
            mPhoto = navArgs<PhotoDetailActivityArgs>().value.photo
            true
        } catch (e: IllegalStateException) {
            Toast.makeText(this, R.string.photo_do_not_exist, Toast.LENGTH_SHORT).show()
            finish()
            false
        }
    }

    override fun initView() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mBinding.share.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(mPhoto.links.html + appComponent.unSplashKeys().urlSuffix)
                startActivity(this)
            }
        }
        mBinding.download.setOnClickListener {
            mVM.download(mPhoto.links.download_location)
        }
        mBinding.setWallpaper.setOnClickListener {
            showSetWallpaperDialog()
        }
        mBinding.userInfo.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(mPhoto.user.links.html + appComponent.unSplashKeys().urlSuffix)
                startActivity(this)
            }
        }

        mBinding.photoImage.updateLayoutParams {
            height = screenWidth * mPhoto.height / mPhoto.width
        }
        mBinding.photo = mPhoto
    }

    override fun observeVM() {
        mVM.photoDetail.observe(this, Observer { res ->
            if (res == null) {
                showGetDetailError()
                return@Observer
            }
            when (res.status) {
                Status.SUCCESS, Status.CACHE -> mBinding.photoDetail = res.data
                Status.ERROR -> showGetDetailError(res.message)
                Status.LOADING -> {
                }
            }
        })
        mVM.downloadResult.observe(this, Observer { res ->
            if (res == null) {
                showDownloadError()
                return@Observer
            }
            when (res.status) {
                Status.SUCCESS, Status.CACHE -> {
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
                Status.ERROR -> showDownloadError(res.message)
                Status.LOADING -> {
                }
            }
        })
        mVM.wallpaperResult.observe(this, Observer { res ->
            if (res == null) {
                showSetWallpaperError()
                return@Observer
            }
            when (res.status) {
                Status.SUCCESS, Status.CACHE -> {
                    AppExecutors.io().execute {
                        try {
                            val future = GlideApp.with(this).download(mPhoto.resizeUrl(screenHeight)).submit()
                            val file = future.get()
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            WallpaperUtil.setWallpaper(this, bitmap, res.data!!.setType)
                            bitmap.recycle()

                            AppExecutors.mainThread().execute {
                                Snackbar.make(
                                    mBinding.root,
                                    getString(R.string.set_wallpaper_success),
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } catch (e: Exception) {
                            showSetWallpaperError(e.message)
                        }
                    }
                }
                Status.ERROR -> showSetWallpaperError(res.message)
                Status.LOADING -> {
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.downloading_appropriate_wallpaper),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun initData() {
        mVM.setPhotoId(mPhoto.id)
    }

    private fun showSetWallpaperDialog() {
        BottomSheetMenuDialogFragment.Builder(this)
            .setSheet(R.menu.menu_wallpaper_set_type)
            .setTitle(R.string.set_wallpaper)
            .setListener(object : SimpleBottomSheetListener() {
                override fun onSheetItemSelected(
                    bottomSheet: BottomSheetMenuDialogFragment,
                    item: MenuItem?,
                    `object`: Any?
                ) {
                    when (item?.itemId) {
                        R.id.menu_set_wallpaper_launcher -> {
                            mVM.requestWallpaper(
                                mPhoto.links.download_location,
                                WallpaperSwitchOption.SetType.HOME_SCREEN
                            )
                        }
                        R.id.menu_set_wallpaper_lock_screen -> {
                            mVM.requestWallpaper(
                                mPhoto.links.download_location,
                                WallpaperSwitchOption.SetType.LOCK_SCREEN
                            )
                        }
                        R.id.menu_set_wallpaper_both -> {
                            mVM.requestWallpaper(mPhoto.links.download_location, WallpaperSwitchOption.SetType.BOTH)
                        }
                    }
                }
            })
            .show(supportFragmentManager)
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