package com.yueban.splashyo.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.yueban.splashyo.R
import com.yueban.splashyo.data.model.PhotoCollection
import com.yueban.splashyo.databinding.ItemCollectionBinding
import com.yueban.splashyo.util.BaseBindingListAdapter

/**
 * @author yueban
 * @date 2019/1/6
 * @email fbzhh007@gmail.com
 */
class CollectionAdapter(
    var itemClickListener: ((PhotoCollection) -> Unit)? = null
) : BaseBindingListAdapter<PhotoCollection, ItemCollectionBinding>(
    diffCallback = object : DiffUtil.ItemCallback<PhotoCollection>() {
        override fun areItemsTheSame(oldItem: PhotoCollection, newItem: PhotoCollection): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoCollection, newItem: PhotoCollection): Boolean {
            return oldItem.copy(rowId = 0) == newItem.copy(rowId = 0)
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ItemCollectionBinding {
        val binding = DataBindingUtil.inflate<ItemCollectionBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_collection,
            parent,
            false
        )
        binding.root.setOnClickListener {
            binding.collection?.let { collection ->
                itemClickListener?.invoke(collection)
            }
        }
        return binding
    }

    override fun bind(binding: ItemCollectionBinding, item: PhotoCollection, payloads: MutableList<Any>) {
        binding.collection = item
    }
}