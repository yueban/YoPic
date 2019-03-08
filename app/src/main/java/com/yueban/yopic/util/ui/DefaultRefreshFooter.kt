package com.yueban.yopic.util.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.yueban.yopic.R
import com.yueban.yopic.databinding.LayoutRefreshFooterBinding

class DefaultRefreshFooter : FrameLayout, RefreshFooter {
    private lateinit var mBinding: LayoutRefreshFooterBinding

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_refresh_footer,
            this,
            true
        )
    }

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int = 0

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        mBinding.noMoreData = noMoreData
        return true
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View = this

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean = false
}