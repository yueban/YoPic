package com.yueban.splashyo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Observable

/**
 * @author yueban
 * @date 2019-02-01
 * @email fbzhh007@gmail.com
 */
@Suppress("LeakingThis")
abstract class BaseViewFragment<Binding : ViewDataBinding> : BaseFragment(), LifecycleProvider<Lifecycle.Event> {
    protected lateinit var mBinding: Binding
    private lateinit var provider: LifecycleProvider<Lifecycle.Event>

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        initVMAndParams(savedInstanceState)
        Observable.just(1).bindToLifecycle(this)
        return mBinding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provider = AndroidLifecycle.createLifecycleProvider(viewLifecycleOwner)

        initView()
        observeVM()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initVMAndParams(savedInstanceState: Bundle?)

    abstract fun initView()

    abstract fun observeVM()

    abstract fun initData()

    override fun lifecycle(): Observable<Lifecycle.Event> = provider.lifecycle()

    override fun <T : Any?> bindUntilEvent(event: Lifecycle.Event): LifecycleTransformer<T> =
        provider.bindUntilEvent(event)

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> = provider.bindToLifecycle()
}