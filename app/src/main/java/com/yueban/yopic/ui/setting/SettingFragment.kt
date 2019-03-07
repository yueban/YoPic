package com.yueban.yopic.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.view.clicks
import com.kennyc.bottomsheet.BottomSheetMenuDialogFragment
import com.yueban.yopic.R
import com.yueban.yopic.data.model.util.WallpaperSwitchOption
import com.yueban.yopic.databinding.FragmentSettingBinding
import com.yueban.yopic.ui.base.BaseViewFragment
import com.yueban.yopic.ui.setting.vm.SettingVM
import com.yueban.yopic.ui.setting.vm.SettingVMFactory
import com.yueban.yopic.util.bottomsheet.SimpleBottomSheetListener
import javax.inject.Inject

class SettingFragment : BaseViewFragment<FragmentSettingBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_setting

    private lateinit var settingVM: SettingVM
    @Inject
    lateinit var settingVMFactory: SettingVMFactory

    override fun initVMAndParams(savedInstanceState: Bundle?) {
        settingVM = ViewModelProviders.of(requireActivity(), settingVMFactory).get(SettingVM::class.java)
    }

    @SuppressLint("CheckResult")
    override fun initView() {
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

        mBinding.option = settingVM.optionObservable
    }

    override fun observeVM() {
    }

    override fun initData() {
    }

    private fun showSourceDialog() {
        BottomSheetMenuDialogFragment.Builder(requireActivity())
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
                            settingVM.optionObservable.sourceType = WallpaperSwitchOption.SourceType.ALL_PHOTOS
                        }
                        R.id.menu_wallpaper_source_collection -> {
                            findNavController().navigate(
                                SettingFragmentDirections.actionSettingFragmentToCollectionFragment()
                                    .setForSetting(true)
                            )
                        }
                    }
                }
            })
            .show(childFragmentManager)
    }

    private fun showSetTypeDialog() {
        BottomSheetMenuDialogFragment.Builder(requireActivity())
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
                            settingVM.optionObservable.setType = WallpaperSwitchOption.SetType.HOME_SCREEN
                        }
                        R.id.menu_set_wallpaper_lock_screen -> {
                            settingVM.optionObservable.setType = WallpaperSwitchOption.SetType.LOCK_SCREEN
                        }
                        R.id.menu_set_wallpaper_both -> {
                            settingVM.optionObservable.setType = WallpaperSwitchOption.SetType.BOTH
                        }
                    }
                }
            })
            .show(childFragmentManager)
    }

    private fun showPeriodDialog() {
        BottomSheetMenuDialogFragment.Builder(requireActivity())
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
                            settingVM.optionObservable.period = WallpaperSwitchOption.Period.MINUTE_30
                        }
                        R.id.menu_wallpaper_period_one_hour -> {
                            settingVM.optionObservable.period = WallpaperSwitchOption.Period.HOUR_1
                        }
                        R.id.menu_wallpaper_period_three_hours -> {
                            settingVM.optionObservable.period = WallpaperSwitchOption.Period.HOUR_3
                        }
                        R.id.menu_wallpaper_period_twelve_hours -> {
                            settingVM.optionObservable.period = WallpaperSwitchOption.Period.HOUR_12
                        }
                        R.id.menu_wallpaper_period_twenty_four_hours -> {
                            settingVM.optionObservable.period = WallpaperSwitchOption.Period.HOUR_24
                        }
                    }
                }
            })
            .show(childFragmentManager)
    }

    override fun onDestroy() {
        super.onDestroy()
        settingVM.refreshWorkIfNeeded()
    }
}