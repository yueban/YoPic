package com.yueban.splashyo.data.repo.model

/**
 * @author yueban
 * @date 2018/12/30
 * @email fbzhh007@gmail.com
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> cache(data: T?): Resource<T> {
            return Resource(Status.CACHE, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}