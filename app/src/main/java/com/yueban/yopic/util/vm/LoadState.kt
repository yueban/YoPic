package com.yueban.yopic.util.vm

class LoadState(
    private val state: UIState,
    private val errorMsg: String?
) {
    private var handledError = false

    val isRefreshing: Boolean
        get() = state == UIState.Refreshing

    val isLoadingMore: Boolean
        get() = state == UIState.LoadingMore

    val isRunning: Boolean
        get() = state == UIState.Refreshing || state == UIState.LoadingMore

    val isSuccess: Boolean
        get() = state == UIState.Success

    val errorMsgIfNotHandled: String?
        get() {
            if (handledError) {
                return null
            }
            handledError = true
            return errorMsg
        }

    enum class UIState {
        Refreshing,
        LoadingMore,
        Success,
        Error,
    }
}