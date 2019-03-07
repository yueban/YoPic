package com.yueban.yopic.util.vm

class LoadState(
    private val state: State,
    private val errorMsg: String?
) {
    private var handledError = false

    val isRefreshing: Boolean
        get() = state == State.Refreshing

    val isLoadingMore: Boolean
        get() = state == State.LoadingMore

    val isRunning: Boolean
        get() = state == State.Refreshing || state == State.LoadingMore

    val isSuccess: Boolean
        get() = state == State.Success

    val errorMsgIfNotHandled: String?
        get() {
            if (handledError) {
                return null
            }
            handledError = true
            return errorMsg
        }

    enum class State {
        Idle,
        Refreshing,
        LoadingMore,
        Success,
        Error,
    }
}