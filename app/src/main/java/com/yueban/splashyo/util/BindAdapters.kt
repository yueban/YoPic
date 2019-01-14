package com.yueban.splashyo.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * @author yueban
 * @date 2019/1/6
 * @email fbzhh007@gmail.com
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imgUrl")
    fun displayImage(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            Glide.with(view).clear(view)
            view.setImageDrawable(null)
            return
        }
        GlideApp.with(view).load(url).into(view)
    }
}
