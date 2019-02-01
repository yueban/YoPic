package com.yueban.splashyo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
abstract class BaseViewFragment<Binding : ViewDataBinding> : BaseFragment() {
    protected lateinit var mBinding: Binding

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        initInjection()
        initVMAndParams(savedInstanceState)

        return mBinding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeVM()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initInjection()

    abstract fun initVMAndParams(savedInstanceState: Bundle?)

    abstract fun initView()

    abstract fun observeVM()

    abstract fun initData()
}