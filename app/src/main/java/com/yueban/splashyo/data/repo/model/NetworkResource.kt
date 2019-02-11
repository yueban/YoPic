package com.yueban.splashyo.data.repo.model

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.yueban.splashyo.data.net.ApiResponse
import com.yueban.splashyo.util.AppExecutors

/**
 * @author yueban
 * @date 2019/1/23
 * @email fbzhh007@gmail.com
 */
abstract class NetworkResource<Type> @MainThread constructor(private val appExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<Type>>()

    init {
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<Type>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = loadFromNet()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiResponse.ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        val data = processResponse(response)
                        saveCallResult(data)
                        appExecutors.mainThread().execute {
                            setValue(Resource.success(data))
                        }
                    }
                }

                is ApiResponse.ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null))
                    }
                }

                is ApiResponse.ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, null))
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<Type>>

    protected open fun onFetchFailed() {}

    @MainThread
    protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<Type>) = response.body

    @MainThread
    protected open fun saveCallResult(data: Type) {
    }

    @MainThread
    protected abstract fun loadFromNet(): LiveData<ApiResponse<Type>>
}