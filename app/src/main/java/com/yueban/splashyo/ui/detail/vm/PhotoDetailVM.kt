package com.yueban.splashyo.ui.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.splashyo.data.model.PhotoDetail
import com.yueban.splashyo.data.repo.PhotoRepo
import com.yueban.splashyo.data.repo.model.Resource
import com.yueban.splashyo.util.rxtransformer.AsyncScheduler
import com.yueban.splashyo.util.rxtransformer.IgnoreErrorTransformer

/**
 * @author yueban
 * @date 2019/1/18
 * @email fbzhh007@gmail.com
 */
class PhotoDetailVM(private val photoRepo: PhotoRepo) : ViewModel() {
    private val _photoId = MutableLiveData<String>()
    private val _photoDetail = MutableLiveData<Resource<PhotoDetail>>()
    private val _downloadLocation = MutableLiveData<String>()
    private val _downloadResult = MutableLiveData<Resource<Any>>()
    private val _requestWallpaper = MutableLiveData<WallpaperRequest>()
    private val _wallpaperResult = MutableLiveData<Resource<WallpaperResponse>>()

    val photoDetail: LiveData<Resource<PhotoDetail>> = _photoDetail
    val downloadResult: LiveData<Resource<Any>> = _downloadResult
    val wallpaperResult: LiveData<Resource<WallpaperResponse>> = _wallpaperResult

    fun setPhotoId(photoId: String) {
        if (_photoId.value == photoId) {
            return
        }
        _photoId.value = photoId
        retryGetDetail()
    }

    fun retryGetDetail() {
        _photoId.value?.let { photoId ->
            photoRepo.getPhotoDetail(photoId)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    _photoDetail.value = Resource.loading(null)
                }
                .doOnError {
                    _photoDetail.value = Resource.error(it.message.orEmpty(), null)
                }
                .compose(IgnoreErrorTransformer.create())
                .subscribe { result ->
                    if (result.isCache) {
                        _photoDetail.value = Resource.cache(result.getNullable())
                    } else {
                        _photoDetail.value = Resource.success(result.getNullable())
                    }
                }
        }
    }

    fun download(downloadLocation: String) {
        if (_downloadLocation.value == downloadLocation) {
            return
        }
        _downloadLocation.value = downloadLocation
        retryDownload()
    }

    fun retryDownload() {
        _downloadLocation.value?.let { downloadLocation ->
            photoRepo.requestDownloadLocation(downloadLocation)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    _downloadResult.value = Resource.loading(null)
                }
                .doOnError {
                    _downloadResult.value = Resource.error(it.message.orEmpty(), null)
                }
                .compose(IgnoreErrorTransformer.create())
                .subscribe { it ->
                    _downloadResult.value = Resource.success(it.getNullable())
                }
        }
    }

    fun requestWallpaper(downloadLocation: String, setType: WallpaperSetType) {
        val newValue = WallpaperRequest(downloadLocation, setType)
        if (_requestWallpaper.value == newValue) {
            return
        }
        _requestWallpaper.value = newValue
        retryRequestWallpaper()
    }

    fun retryRequestWallpaper() {
        _requestWallpaper.value?.let { request ->
            photoRepo.requestDownloadLocation(request.downloadLocation)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    _wallpaperResult.value = Resource.loading(null)
                }
                .doOnError {
                    _wallpaperResult.value = Resource.error(it.message.orEmpty(), null)
                }
                .compose(IgnoreErrorTransformer.create())
                .subscribe { it ->
                    val response = WallpaperResponse(it.getNullable(), request.setType)
                    _wallpaperResult.value = Resource.success(response)
                }
        }
    }
}

data class WallpaperRequest(val downloadLocation: String, val setType: WallpaperSetType)

class WallpaperResponse(val res: Any?, val setType: WallpaperSetType)

enum class WallpaperSetType {
    HOME_SCREEN, LOCK_SCREEN, BOTH
}