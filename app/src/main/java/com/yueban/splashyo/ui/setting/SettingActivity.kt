package com.yueban.splashyo.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.util.WallpaperSwitchOption
import com.yueban.splashyo.databinding.ActivitySettingBinding
import com.yueban.splashyo.ui.base.BaseViewActivity
import com.yueban.splashyo.util.PrefKey
import com.yueban.splashyo.util.PrefManager
import javax.inject.Inject

class SettingActivity : BaseViewActivity<ActivitySettingBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_setting
    @Inject
    lateinit var prefManager: PrefManager
    lateinit var option: WallpaperSwitchOption

    override fun initVMAndParams(savedInstanceState: Bundle?): Boolean {
        option = prefManager.getObject(PrefKey.WALLPAPER_SWITCH_OPTION, WallpaperSwitchOption::class.java)
            ?: WallpaperSwitchOption()
        return true
    }

    @SuppressLint("CheckResult")
    override fun initView() {
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setTitle(R.string.setting)

        // set listeners
        mBinding.layoutWallpaperEnabled.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show()
            }
        mBinding.layoutWallpaperSource.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                Toast.makeText(this, "Source", Toast.LENGTH_SHORT).show()
            }
        mBinding.layoutWallpaperSetType.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                Toast.makeText(this, "SetType", Toast.LENGTH_SHORT).show()
            }
        mBinding.layoutWallpaperPeriod.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                Toast.makeText(this, "Source", Toast.LENGTH_SHORT).show()
            }
        mBinding.layoutWallpaperOnlyWifi.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                Toast.makeText(this, "OnlyWifi", Toast.LENGTH_SHORT).show()
            }

        refreshView()
    }

    private fun refreshView() {
        mBinding.swWallpaperEnabled.isEnabled = option.enabled
        mBinding.tvWallpaperSource.text = when (option.sourceType) {
            WallpaperSwitchOption.SourceType.ALL_PHOTOS -> getString(R.string.all_photos)
            WallpaperSwitchOption.SourceType.COLLECTION -> option.collectionName
            else -> ""
        }
        mBinding.tvWallpaperPeriod.text = when (option.period) {
            WallpaperSwitchOption.Period.MINUTE_30 -> getString(R.string.thirty_minutes)
            WallpaperSwitchOption.Period.HOUR_1 -> getString(R.string.one_hour)
            WallpaperSwitchOption.Period.HOUR_3 -> getString(R.string.three_hours)
            WallpaperSwitchOption.Period.HOUR_12 -> getString(R.string.twelve_hours)
            WallpaperSwitchOption.Period.HOUR_24 -> getString(R.string.twenty_four_hours)
            else -> ""
        }
        mBinding.tvWallpaperSetType.text = when (option.setType) {
            WallpaperSwitchOption.SetType.HOME_SCREEN -> getString(R.string.home_screen)
            WallpaperSwitchOption.SetType.LOCK_SCREEN -> getString(R.string.lock_screen)
            WallpaperSwitchOption.SetType.BOTH -> getString(R.string.home_screen_and_lock_screen)
            else -> ""
        }
        mBinding.swWallpaperOnlyWifi.isEnabled = option.onlyInWifi
    }

    override fun observeVM() {
    }

    override fun initData() {
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