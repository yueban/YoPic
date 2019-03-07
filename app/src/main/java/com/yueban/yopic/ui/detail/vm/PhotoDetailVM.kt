package com.yueban.yopic.ui.detail.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yueban.yopic.data.model.PhotoDetail
import com.yueban.yopic.data.model.util.WallpaperSwitchOption
import com.yueban.yopic.data.repo.PhotoRepo
import com.yueban.yopic.data.repo.model.Resource
import com.yueban.yopic.util.rxtransformer.AsyncScheduler
import com.yueban.yopic.util.rxtransformer.IgnoreErrorTransformer

/**
 * @author yueban
 * @date 2019/1/18
 * @email fbzhh007@gmail.com
 */
class PhotoDetailVM(private val photoRepo: PhotoRepo) : ViewModel() {
    private val _photoId = MutableLiveData<String>()
    private val _photoDetail = MutableLiveData<Resource<PhotoDetail>>()
    private val _downloadRequest = MutableLiveData<String>()
    private val _downloadResult = MutableLiveData<Resource<Any>>()
    private val _wallpaperRequest = MutableLiveData<WallpaperRequest>()
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
        if (_downloadRequest.value == downloadLocation) {
            // TODO("提示 正在下载")
            return
        }
        _downloadRequest.value = downloadLocation
        retryDownload()
    }

    fun retryDownload() {
        _downloadRequest.value?.let { downloadLocation ->
            photoRepo.requestDownloadLocation(downloadLocation)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    _downloadResult.value = Resource.loading(null)
                }
                .doOnError {
                    _downloadResult.value = Resource.error(it.message.orEmpty(), null)
                    _downloadRequest.value = null
                }
                .compose(IgnoreErrorTransformer.create())
                .subscribe { it ->
                    _downloadResult.value = Resource.success(it.getNullable())
                    _downloadRequest.value = null
                }
        }
    }

    fun requestWallpaper(downloadLocation: String, @WallpaperSwitchOption.SetType setType: Int) {
        val newValue = WallpaperRequest(downloadLocation, setType)
        if (_wallpaperRequest.value == newValue) {
            // TODO("提示 正在下载壁纸")
            return
        }
        _wallpaperRequest.value = newValue
        retryRequestWallpaper()
    }

    fun retryRequestWallpaper() {
        _wallpaperRequest.value?.let { request ->
            photoRepo.requestDownloadLocation(request.downloadLocation)
                .compose(AsyncScheduler.create())
                .doOnSubscribe {
                    _wallpaperResult.value = Resource.loading(null)
                }
                .doOnError {
                    _wallpaperResult.value = Resource.error(it.message.orEmpty(), null)
                    _wallpaperRequest.value = null
                }
                .compose(IgnoreErrorTransformer.create())
                .subscribe { it ->
                    val response = WallpaperResponse(it.getNullable(), request.setType)
                    _wallpaperResult.value = Resource.success(response)
                    _wallpaperRequest.value = null
                }
        }
    }
}

data class WallpaperRequest(val downloadLocation: String, @WallpaperSwitchOption.SetType val setType: Int)

class WallpaperResponse(val res: Any?, @WallpaperSwitchOption.SetType val setType: Int)