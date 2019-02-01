package com.yueban.splashyo.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
abstract class BaseViewActivity<Binding : ViewDataBinding> : BaseActivity() {
    protected lateinit var mBinding: Binding

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, getLayoutId())

        if (!initVMAndParams(savedInstanceState)) {
            return
        }
        initView()
        observeVM()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * @return return true as default;
     *         return false if there's something wrong with parameters, then the initialization logic will be interrupt.
     */
    abstract fun initVMAndParams(savedInstanceState: Bundle?): Boolean

    abstract fun initView()

    abstract fun observeVM()

    abstract fun initData()
}