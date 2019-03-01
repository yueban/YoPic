package com.yueban.splashyo.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.util.WallpaperSwitchOption
import com.yueban.splashyo.data.model.util.WallpaperSwitchOptionObservable
import com.yueban.splashyo.databinding.ActivitySettingBinding
import com.yueban.splashyo.ui.base.BaseViewActivity
import com.yueban.splashyo.util.PrefKey
import com.yueban.splashyo.util.PrefManager
import com.yueban.splashyo.util.bottomsheet.SimpleBottomSheetListener
import com.yueban.splashyo.worker.WorkerUtil
import javax.inject.Inject

class SettingActivity : BaseViewActivity<ActivitySettingBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_setting
    @Inject
    lateinit var prefManager: PrefManager
    @Inject
    lateinit var workerUtil: WorkerUtil
    lateinit var option: WallpaperSwitchOption
    lateinit var optionObservable: WallpaperSwitchOptionObservable

    override fun initVMAndParams(savedInstanceState: Bundle?): Boolean {
        option = prefManager.getObject(PrefKey.WALLPAPER_SWITCH_OPTION, WallpaperSwitchOption::class.java)
            ?: WallpaperSwitchOption()
        optionObservable = WallpaperSwitchOptionObservable(option)
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
                mBinding.swWallpaperEnabled.isChecked = !mBinding.swWallpaperEnabled.isChecked
            }
        mBinding.layoutWallpaperSource.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                showSourceDialog()
            }
        mBinding.layoutWallpaperSetType.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                showSetTypeDialog()
            }
        mBinding.layoutWallpaperPeriod.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                showPeriodDialog()
            }
        mBinding.layoutWallpaperOnlyWifi.clicks()
            .compose(bindToLifecycle())
            .subscribe {
                mBinding.swWallpaperOnlyWifi.isChecked = !mBinding.swWallpaperOnlyWifi.isChecked
            }

        mBinding.option = optionObservable
    }

    private fun showSourceDialog() {
        BottomSheetMenuDialogFragment.Builder(this)
            .setSheet(R.menu.menu_wallpaper_source)
            .setTitle(R.string.wallpaper_source)
            .setListener(object : SimpleBottomSheetListener() {
                override fun onSheetItemSelected(
                    bottomSheet: BottomSheetMenuDialogFragment,
                    item: MenuItem?,
                    `object`: Any?
                ) {
                    when (item?.itemId) {
                        R.id.menu_wallpaper_source_all_photos -> {
                            optionObservable.sourceType = WallpaperSwitchOption.SourceType.ALL_PHOTOS
                        }
                        R.id.menu_wallpaper_source_collection -> {
                            Toast.makeText(this@SettingActivity, "todo: goto collection view", Toast.LENGTH_SHORT)
                                .show()
                            // TODO(goto collection view)
                        }
                    }
                }
            })
            .show(supportFragmentManager)
    }

    private fun showSetTypeDialog() {
        BottomSheetMenuDialogFragment.Builder(this)
            .setSheet(R.menu.menu_wallpaper_set_type)
            .setTitle(R.string.wallpaper_set_type)
            .setListener(object : SimpleBottomSheetListener() {
                override fun onSheetItemSelected(
                    bottomSheet: BottomSheetMenuDialogFragment,
                    item: MenuItem?,
                    `object`: Any?
                ) {
                    when (item?.itemId) {
                        R.id.menu_set_wallpaper_launcher -> {
                            optionObservable.setType = WallpaperSwitchOption.SetType.HOME_SCREEN
                        }
                        R.id.menu_set_wallpaper_lock_screen -> {
                            optionObservable.setType = WallpaperSwitchOption.SetType.LOCK_SCREEN
                        }
                        R.id.menu_set_wallpaper_both -> {
                            optionObservable.setType = WallpaperSwitchOption.SetType.BOTH
                        }
                    }
                }
            })
            .show(supportFragmentManager)
    }

    private fun showPeriodDialog() {
        BottomSheetMenuDialogFragment.Builder(this)
            .setSheet(R.menu.menu_wallpaper_period)
            .setTitle(R.string.switch_period)
            .setListener(object : SimpleBottomSheetListener() {
                override fun onSheetItemSelected(
                    bottomSheet: BottomSheetMenuDialogFragment,
                    item: MenuItem?,
                    `object`: Any?
                ) {
                    when (item?.itemId) {
                        R.id.menu_wallpaper_period_thirty_minutes -> {
                            optionObservable.period = WallpaperSwitchOption.Period.MINUTE_30
                        }
                        R.id.menu_wallpaper_period_one_hour -> {
                            optionObservable.period = WallpaperSwitchOption.Period.HOUR_1
                        }
                        R.id.menu_wallpaper_period_three_hours -> {
                            optionObservable.period = WallpaperSwitchOption.Period.HOUR_3
                        }
                        R.id.menu_wallpaper_period_twelve_hours -> {
                            optionObservable.period = WallpaperSwitchOption.Period.HOUR_12
                        }
                        R.id.menu_wallpaper_period_twenty_four_hours -> {
                            optionObservable.period = WallpaperSwitchOption.Period.HOUR_24
                        }
                    }
                }
            })
            .show(supportFragmentManager)
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

    override fun onDestroy() {
        super.onDestroy()

        val newOption = optionObservable.toOption()
        if (newOption != option) {
            prefManager.put(PrefKey.WALLPAPER_SWITCH_OPTION, newOption, WallpaperSwitchOption::class.java)
            workerUtil.refreshWallpaperChangeTask()
        }
    }
}