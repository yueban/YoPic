package com.yueban.splashyo.util.vm

class LoadState(val isRefreshing: Boolean, val isLoadingMore: Boolean, private val errorMsg: String?) {
    private var handledError = false

    val errorMsgIfNotHandled: String?
        get() {
            if (handledError) {
                return null
            }
            handledError = true
            return errorMsg
        }

    val isRunning: Boolean
        get() = isRefreshing || isLoadingMore
}