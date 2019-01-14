package com.yueban.splashyo.util

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * @author yueban
 * @date 2019/1/7
 * @email fbzhh007@gmail.com
 */
abstract class BaseBindingListAdapter<T, V : ViewDataBinding>(
    appExecutors: AppExecutors,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseBindingViewHolder<V>>(
    AsyncDifferConfig.Builder<T>(diffCallback)
        .setBackgroundThreadExecutor(appExecutors.diskIO())
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<V> {
        val binding = createBinding(parent)
        return BaseBindingViewHolder(binding)
    }

    abstract fun createBinding(parent: ViewGroup): V

    final override fun onBindViewHolder(holder: BaseBindingViewHolder<V>, position: Int) {}

    final override fun onBindViewHolder(holder: BaseBindingViewHolder<V>, position: Int, payloads: MutableList<Any>) {
        bind(holder.binding, getItem(position), payloads)
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T, payloads: MutableList<Any>)
}