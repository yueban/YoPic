package com.yueban.splashyo.util.bindingadapters

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.yueban.splashyo.R
import com.yueban.splashyo.util.GlideApp
import com.yueban.splashyo.util.GlideOptions.bitmapTransform
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

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
            GlideApp.with(view)
                .load(url).thumbnail(GlideApp.with(view).load(previewUrl)).into(view)
        } else if (previewColor != null) {
            GlideApp.with(view).load(url).placeholder(ColorDrawable(previewColor)).into(view)
        } else {
            GlideApp.with(view).load(url).into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("visible")
    fun visible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("avatarUrl")
    fun displayAvatarImage(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            Glide.with(view).clear(view)
            view.setImageDrawable(null)
            return
        }

        GlideApp.with(view)
            .load(url)
            .apply(bitmapTransform(RoundedCornersTransformation(1000, 0)))
            .placeholder(R.drawable.placeholder_avatar)
            .into(view)
    }
}
