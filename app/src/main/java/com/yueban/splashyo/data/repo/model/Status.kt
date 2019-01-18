package com.yueban.splashyo.data.repo.model

/**
 * @author yueban
 * @date 2018/12/30
 * @email fbzhh007@gmail.com
 */
enum class Status {
    SUCCESS,
    /**
     * appears only when there is a api request. if there's no api request, the cache data will return as a [SUCCESS] status
     */
    CACHE,
    ERROR,
    LOADING,
}