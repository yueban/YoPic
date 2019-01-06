package com.yueban.splashyo.util

import androidx.lifecycle.LiveData

/**
 * @author yueban
 * @date 2019/1/1
 * @email fbzhh007@gmail.com
 */
class NullLiveData<T : Any?> private constructor() : LiveData<T>() {
    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return NullLiveData()
        }
    }
}