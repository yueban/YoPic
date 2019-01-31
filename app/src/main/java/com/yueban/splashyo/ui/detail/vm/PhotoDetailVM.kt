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
    private val _downloadLocation = MutableLiveData<String>()
    private val _requestWallpaper = MutableLiveData<String>()

    val photoDetail: LiveData<Resource<PhotoDetail>> = Transformations.switchMap(_photoId) {
        if (it.isNullOrEmpty()) {
            NullLiveData.create()
        } else {
            photoRepo.getPhotoDetail(it)
        }
    }

    val requestDownloadResult: LiveData<Resource<Any>> = Transformations.switchMap(_downloadLocation) {
        if (it.isNullOrEmpty()) {
            NullLiveData.create()
        } else {
            photoRepo.requestDownloadLocation(it)
        }
    }

    val requestWallpaperResult: LiveData<Resource<Any>> = Transformations.switchMap(_requestWallpaper) {
        if (it.isNullOrEmpty()) {
            NullLiveData.create()
        } else {
            photoRepo.requestDownloadLocation(it)
        }
    }

    fun retryGetDetail() {
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

    fun download(downloadLocation: String) {
        if (_downloadLocation.value == downloadLocation) {
            return
        }
        _downloadLocation.value = downloadLocation
    }

    fun retryDownload() {
        _downloadLocation.value?.let {
            _downloadLocation.value = it
        }
    }

    fun requestWallpaper(downloadLocation: String) {
        if (_requestWallpaper.value == downloadLocation) {
            return
        }
        _requestWallpaper.value = downloadLocation
    }

    fun retryRequestWallpaper() {
        _requestWallpaper.value?.let {
            _requestWallpaper.value = it
        }
    }
}