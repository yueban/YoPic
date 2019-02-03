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
    private val _requestWallpaper = MutableLiveData<WallpaperRequest>()

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

    val requestWallpaperResult: LiveData<WallpaperResponse> = Transformations.switchMap(_requestWallpaper) { request ->
        if (request == null) {
            NullLiveData.create()
        } else {
            Transformations.map(photoRepo.requestDownloadLocation(request.downloadLocation)) { res ->
                WallpaperResponse(res, request.setType)
            }
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

    fun requestWallpaper(downloadLocation: String, setType: WallpaperSetType) {
        val newValue = WallpaperRequest(downloadLocation, setType)
        if (_requestWallpaper.value == newValue) {
            return
        }
        _requestWallpaper.value = newValue
    }

    fun retryRequestWallpaper() {
        _requestWallpaper.value?.let {
            _requestWallpaper.value = it
        }
    }
}

data class WallpaperRequest(val downloadLocation: String, val setType: WallpaperSetType)

class WallpaperResponse(val res: Resource<Any>, val setType: WallpaperSetType)

enum class WallpaperSetType {
    HOME_SCREEN, LOCK_SCREEN, BOTH
}