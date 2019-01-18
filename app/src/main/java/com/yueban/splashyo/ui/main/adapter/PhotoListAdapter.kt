package com.yueban.splashyo.ui.main.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.yueban.splashyo.data.model.Photo
import com.yueban.splashyo.databinding.ItemPhotoBinding
import com.yueban.splashyo.util.AppExecutors
import com.yueban.splashyo.util.BaseBindingListAdapter

/**
 * @author yueban
 * @date 2019/1/15
 * @email fbzhh007@gmail.com
 */
class PhotoListAdapter(
    appExecutors: AppExecutors,
    val spanCount: Int,
    val itemSpacing: Int,
    var itemClickListener: ((imgView: ImageView, photo: Photo) -> Unit)? = null
) : BaseBindingListAdapter<Photo, ItemPhotoBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ItemPhotoBinding {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.root.setOnClickListener {
            binding.photo?.let { photo ->
                itemClickListener?.invoke(binding.photoImage, photo)
            }
        }
        return binding
    }

    override fun bind(binding: ItemPhotoBinding, item: Photo, payloads: MutableList<Any>) {
        val params = binding.photoImage.layoutParams
        val itemWidth = (Resources.getSystem().displayMetrics.widthPixels - (spanCount + 1) * itemSpacing) / spanCount
        params.height = itemWidth * item.height / item.width
        binding.photoImage.layoutParams = params

        binding.photo = item
    }
}