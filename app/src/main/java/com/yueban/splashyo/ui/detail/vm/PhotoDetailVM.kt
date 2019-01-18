package com.yueban.splashyo.ui.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.PhotoDetail
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.util.NullLiveData

/**
 * @author yueban
 * @date 2019/1/18
 * @email fbzhh007@gmail.com
 */
class PhotoDetailVM(private val photoRepo: PhotoRepo) : ViewModel() {
    private val _photoId = MutableLiveData<String>()
    val photoDetail: LiveData<Resource<PhotoDetail>> = Transformations.switchMap(_photoId) {
        if (it.isNullOrEmpty()) {
            NullLiveData.create()
        } else {
            photoRepo.getPhotoDetail(it)
        }
    }

    fun retry() {
        _photoId.value?.let {
            _photoId.value = it
        }
    }

    fun setPhotoId(photoId: String) {
        if (_photoId.value == photoId) {
            return
        }
        _photoId.value = photoId
    }
}