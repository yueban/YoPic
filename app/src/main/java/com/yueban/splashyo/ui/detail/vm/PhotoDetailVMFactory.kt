package com.yueban.splashyo.ui.detail.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yueban.splashyo.data.repo.PhotoRepo

/**
 * @author yueban
 * @date 2019/1/18
 * @email fbzhh007@gmail.com
 */
class PhotoDetailVMFactory(private val photoRepo: PhotoRepo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoDetailVM(photoRepo) as T
    }
}