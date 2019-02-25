package com.yueban.splashyo.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.LifecycleTransformer
import io.reactivex.Observable

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
abstract class BaseViewActivity<Binding : ViewDataBinding> : BaseActivity(), LifecycleProvider<Lifecycle.Event> {
    protected lateinit var mBinding: Binding
    @Suppress("LeakingThis")
    private val provider: LifecycleProvider<Lifecycle.Event> = AndroidLifecycle.createLifecycleProvider(this)

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

    override fun lifecycle(): Observable<Lifecycle.Event> = provider.lifecycle()

    override fun <T : Any?> bindUntilEvent(event: Lifecycle.Event): LifecycleTransformer<T> =
        provider.bindUntilEvent(event)

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> = provider.bindToLifecycle()
}