package com.yueban.splashyo.util.vm

sealed class BaseLoadState(val isRefreshing: Boolean, private val errorMsg: String?) {
    private var handledError = false

    val errorMsgIfNotHandled: String?
        get() {
            if (handledError) {
                return null
            }
            handledError = true
            return errorMsg
        }
}

class LoadState constructor(
    isRefreshing: Boolean,
    errorMsg: String?
) : BaseLoadState(isRefreshing, errorMsg)

class ListLoadState constructor(
    isRefreshing: Boolean,
    val isLoadingMore: Boolean,
    errorMsg: String?
) : BaseLoadState(isRefreshing, errorMsg) {
    val isRunning: Boolean
        get() = isRefreshing || isLoadingMore
}


