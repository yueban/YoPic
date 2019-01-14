package com.yueban.splashyo.util

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author yueban
 * @date 2019/1/7
 * @email fbzhh007@gmail.com
 */
class BaseBindingViewHolder<out T : ViewDataBinding> constructor(val binding: T) :
    RecyclerView.ViewHolder(binding.root)