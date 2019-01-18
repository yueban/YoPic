package com.yueban.splashyo.util

import android.graphics.drawable.ColorDrawable
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
    @BindingAdapter("imgUrl", "previewUrl", "previewColor", requireAll = false)
    fun displayImage(view: ImageView, url: String?, previewUrl: String?, previewColor: Int?) {
        if (url.isNullOrEmpty()) {
            Glide.with(view).clear(view)
            view.setImageDrawable(null)
            return
        }

        if (!previewUrl.isNullOrEmpty()) {
            GlideApp.with(view).load(url).thumbnail(GlideApp.with(view).load(previewUrl)).into(view)
        } else if (previewColor != null) {
            GlideApp.with(view).load(url).placeholder(ColorDrawable(previewColor)).into(view)
        } else {
            GlideApp.with(view).load(url).into(view)
        }
    }
}
