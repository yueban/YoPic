package com.yueban.yopic.data.repo

import com.quanturium.bouquet.annotations.RxLogger
import com.yueban.yopic.data.Optional
import com.yueban.yopic.data.local.db.PhotoDao
import com.yueban.yopic.data.model.Photo
import com.yueban.yopic.data.model.PhotoCollection
import com.yueban.yopic.data.model.PhotoDetail
import com.yueban.yopic.data.net.UnSplashService
import com.yueban.yopic.ui.main.vm.PhotoListVM
import com.yueban.yopic.util.di.scope.AppScope
import com.yueban.yopic.util.rxtransformer.MarkAsCacheTransformer
import com.yueban.yopic.util.rxtransformer.RoomOptionalTransformer
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author yueban
 * @date 2018/12/30
 * @email fbzhh007@gmail.com
 */
@AppScope
class PhotoRepo
@Inject constructor(
    private val photoDao: PhotoDao,
    private val service: UnSplashService
) {
    @RxLogger(RxLogger.Scope.SUMMARY)
    fun getPhotoDetail(photoId: String): Flowable<Optional<PhotoDetail>> {
        return photoDao.getPhotoDetail(photoId)
            .compose(RoomOptionalTransformer.create())
            .toFlowable()
            .flatMap {
                val cacheExpired = if (it.isNull) {
                    true
                } else {
                    val timeSpan = System.currentTimeMillis() - it.get().cacheUpdateAt
                    timeSpan > 1000 * 60
                }

                if (!cacheExpired) {
                    Single.just(it).toFlowable()
                } else {
                    Single.just(it).concatWith(service.photoDetail(photoId))
                }
            }
    }

    @RxLogger(RxLogger.Scope.SUMMARY)
    fun requestDownloadLocation(downloadLocation: String): Single<Optional<Any>> =
        service.requestDownloadLocation(downloadLocation)

    @RxLogger(RxLogger.Scope.SUMMARY)
    fun getCollections(
        featured: Boolean,
        page: Int,
        firstPage: Int = 1,
        loadCacheOnFirstPage: Boolean = true,
        clearCacheOnFirstPage: Boolean = true
    ): Flowable<Optional<List<PhotoCollection>>> {
        val netSource =
            if (featured) {
                service.collectionsFeatured(page)
            } else {
                service.collections(page)
            }.doOnSuccess {
                if (!it.isNull) {
                    if (clearCacheOnFirstPage && page == firstPage) {
                        photoDao.deleteAllCollections()
                    }
                    photoDao.insertCollections(it.get())
                }
            }

        return if (page == firstPage && loadCacheOnFirstPage) {
            if (featured) {
                photoDao.getFeaturedCollections()
            } else {
                photoDao.getCollections()
            }.compose(RoomOptionalTransformer.create())
                .compose(MarkAsCacheTransformer.create())
                .concatWith(netSource)
        } else {
            netSource.toFlowable()
        }
    }

    @RxLogger(RxLogger.Scope.SUMMARY)
    fun getPhotos(
        cacheLabel: String,
        page: Int,
        firstPage: Int = 1,
        loadCacheOnFirstPage: Boolean = true,
        clearCacheOnFirstPage: Boolean = true
    ): Flowable<Optional<List<Photo>>> {
        val netSource =
            if (cacheLabel == PhotoListVM.CACHE_LABEL_ALL) {
                service.photos(page)
            } else {
                service.photosByCollection(cacheLabel, page)
            }.doOnSuccess {
                if (!it.isNull) {
                    // add cache label
                    it.get().forEach { photo -> photo.cacheLabel = cacheLabel }
                    if (clearCacheOnFirstPage && page == firstPage) {
                        photoDao.deleteAllPhotos(cacheLabel)
                    }
                    photoDao.insertPhotos(it.get())
                }
            }

        return if (page == firstPage && loadCacheOnFirstPage) {
            photoDao.getPhotos(cacheLabel)
                .compose(RoomOptionalTransformer.create())
                .compose(MarkAsCacheTransformer.create())
                .concatWith(netSource)
        } else {
            netSource.toFlowable()
        }
    }
}
