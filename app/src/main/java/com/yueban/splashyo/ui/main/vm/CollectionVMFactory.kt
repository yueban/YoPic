package com.yueban.splashyo.ui.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yueban.splashyo.data.repo.PhotoRepo

/**
 * @author yueban
 * @date 2019/1/5
 * @email fbzhh007@gmail.com
 */
class CollectionVMFactory(private val photoRepo: PhotoRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CollectionVM(photoRepo) as T
    }
}