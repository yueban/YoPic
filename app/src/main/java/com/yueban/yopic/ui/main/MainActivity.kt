package com.yueban.yopic.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yueban.yopic.R
import com.yueban.yopic.databinding.ActivityMainBinding
import com.yueban.yopic.ui.base.BaseViewActivity
import com.yueban.yopic.util.UNSPLASH_URL

class MainActivity : BaseViewActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initVMAndParams(savedInstanceState: Bundle?): Boolean = true

    override fun initView() {
        mBinding.navigation.menu.findItem(R.id.aboutActivity).title =
            getString(R.string.about_app, getString(R.string.app_name))

        setSupportActionBar(mBinding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        mBinding.toolbar.setupWithNavController(navController, mBinding.drawerLayout)
        mBinding.navigation.setupWithNavController(navController)

        mBinding.unsplashLabel.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(UNSPLASH_URL)
                startActivity(this)
            }
            mBinding.drawerLayout.closeDrawer(mBinding.navigation)
        }
    }

    override fun observeVM() {
    }

    override fun initData() {
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }
}