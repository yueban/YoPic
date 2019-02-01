package com.yueban.splashyo.ui.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yueban.splashyo.R
import com.yueban.splashyo.databinding.ActivityMainBinding
import com.yueban.splashyo.ui.base.BaseViewActivity

class MainActivity : BaseViewActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initVMAndParams(savedInstanceState: Bundle?): Boolean = true

    override fun initView() {
        setSupportActionBar(mBinding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        mBinding.toolbar.setupWithNavController(navController, mBinding.drawerLayout)
        mBinding.navigation.setupWithNavController(navController)
    }

    override fun observeVM() {
    }

    override fun initData() {
    }
}