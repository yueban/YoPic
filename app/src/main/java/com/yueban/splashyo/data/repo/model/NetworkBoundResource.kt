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
abstract class NetworkBoundResource<Type>
@MainThread constructor(private val appExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<Type>>()

    init {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromCache()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                if (!skipCacheResultWhenFetchFromNet(data)) {
                    setValue(Resource.cache(data))
                }
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<Type>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<Type>) {
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
                        val data = processResponse(response)
                        saveCallResult(data)
                        appExecutors.mainThread().execute {
                            if (resultFromCache()) {
                                // request a new liveData from db, cause we have already saveCallResult in db. This can make result always use dbSource as its dataSource
                                result.addSource(loadFromCache()) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            } else {
                                setValue(Resource.success(data))
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

    fun asLiveData() = result as LiveData<Resource<Type>>

    @MainThread
    protected open fun processResponse(response: ApiResponse.ApiSuccessResponse<Type>) = response.body

    @MainThread
    protected open fun resultFromCache(): Boolean = false

    @MainThread
    protected open fun skipCacheResultWhenFetchFromNet(data: Type?): Boolean = true

    @MainThread
    protected abstract fun saveCallResult(data: Type)

    @MainThread
    protected abstract fun shouldFetch(data: Type?): Boolean

    @MainThread
    protected abstract fun loadFromCache(): LiveData<Type>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<Type>>
}