package com.yueban.splashyo.ui.setting

import android.os.Bundle
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setTitle(R.string.setting)
    }

    override fun observeVM() {
    }

    override fun initData() {
    }
}