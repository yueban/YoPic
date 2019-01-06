package com.yueban.splashyo.data.repo.model

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.yueban.splashyo.data.net.ApiResponse
import com.yueban.splashyo.util.AppExecutors

/**
 * @author yueban
 * @date 2018/12/30
 * @email fbzhh007@gmail.com
 */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromCache()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiResponse.ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            // request a new livedata from db, cause we have already saveCallResult in db. This can make result always use dbSource as its dataSource
                            result.addSource(loadFromCache()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }

                is ApiResponse.ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        // reload cache from db if api response is empty
                        result.addSource(loadFromCache()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }

                is ApiResponse.ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        // reload cache from db if api request failed
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @MainThread
    protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<RequestType>) = response.body

    @MainThread
    protected abstract fun saveCallResult(data: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromCache(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}