package com.yueban.splashyo.ui.setting

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.yueban.splashyo.R
import com.yueban.splashyo.databinding.ActivitySettingBinding
import com.yueban.splashyo.ui.base.BaseViewActivity

class SettingActivity : BaseViewActivity<ActivitySettingBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_setting

    override fun initVMAndParams(savedInstanceState: Bundle?): Boolean {
        return true
    }

    override fun initView() {
        setSupportActionBar(mBinding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        // pass emptySet to make no top-destId, to show back button in toolbar on start destination
        mBinding.toolbar.setupWithNavController(navController, AppBarConfiguration(emptySet(), null) {
            if (navController.currentDestination?.id == R.id.settingFragment) {
                finish()
                true
            } else {
                false
            }
        })

        setTitle(R.string.setting)
    }

    override fun observeVM() {
    }

    override fun initData() {
    }
}