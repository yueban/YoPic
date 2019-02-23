package com.yueban.splashyo.data.net

import com.yueban.splashyo.data.Optional
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import java.lang.reflect.Type

internal class RxJava2CallAdapter<R>(
    private val responseType: Type,
    private val scheduler: Scheduler?,
    private val isOptional: Boolean,
    private val isBody: Boolean,
    private val isAsync: Boolean,
    private val isFlowable: Boolean,
    private val isSingle: Boolean,
    private val isMaybe: Boolean,
    private val isCompletable: Boolean
) : CallAdapter<R, Any> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): Any {
        val responseObservable = if (isAsync)
            CallEnqueueObservable(call)
        else
            CallExecuteObservable(call)

        var observable: Observable<*>
        if (isBody || isOptional) {
            observable = responseObservable.map { response ->
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        null
                    } else {
                        body
                    }
                } else {
                    throw HttpException(response)
                }
            }
            if (isOptional) {
                observable = observable.map {
                    if (it == null) {
                        Optional.empty()
                    } else {
                        Optional(it)
                    }
                }.onErrorReturn {
                    if (it is NullPointerException) {
                        Optional.empty()
                    } else {
                        throw it
                    }
                }
            }
        } else {
            observable = responseObservable
        }


        if (scheduler != null) {
            observable = observable.subscribeOn(scheduler)
        }

        if (isFlowable) {
            return observable.toFlowable(BackpressureStrategy.LATEST)
        }
        if (isSingle) {
            return observable.singleOrError()
        }
        if (isMaybe) {
            return observable.singleElement()
        }
        return if (isCompletable) {
            observable.ignoreElements()
        } else RxJavaPlugins.onAssembly(observable)
    }
}