package com.yueban.splashyo.data.net

import com.yueban.splashyo.data.Optional
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RxJava2CallAdapterFactory private constructor(
    private val scheduler: Scheduler?, private val isAsync: Boolean
) :
    CallAdapter.Factory() {

    override fun get(
        returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawType = CallAdapter.Factory.getRawType(returnType)

        if (rawType == Completable::class.java) {
            // Completable is not parameterized (which is what the rest of this method deals with) so it
            // can only be created with a single configuration.
            return RxJava2CallAdapter<Void>(
                Void::class.java, scheduler, false, false, isAsync, false, false, false, true
            )
        }

        val isFlowable = rawType == Flowable::class.java
        val isSingle = rawType == Single::class.java
        val isMaybe = rawType == Maybe::class.java
        if (rawType != Observable::class.java && !isFlowable && !isSingle && !isMaybe) {
            return null
        }

        var isOptional = false
        var isBody = false
        val responseType: Type
        if (returnType !is ParameterizedType) {
            val name = if (isFlowable)
                "Flowable"
            else if (isSingle)
                "Single"
            else if (isMaybe) "Maybe" else "Observable"
            throw IllegalStateException(
                name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>"
            )
        }

        val observableType = CallAdapter.Factory.getParameterUpperBound(0, returnType)
        val rawObservableType = CallAdapter.Factory.getRawType(observableType)

        when (rawObservableType) {
            Response::class.java -> {
                if (observableType !is ParameterizedType) {
                    throw IllegalStateException(("Response must be parameterized" + " as Response<Foo> or Response<? extends Foo>"))
                }
                responseType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
            }
            Optional::class.java -> {
                if (observableType !is ParameterizedType) {
                    throw IllegalStateException(("Optional must be parameterized" + " as Optional<Foo> or Optional<? extends Foo>"))
                }
                responseType = CallAdapter.Factory.getParameterUpperBound(0, observableType)
                isOptional = true
            }
            else -> {
                responseType = observableType
                isBody = true
            }
        }

        return RxJava2CallAdapter<Any>(
            responseType, scheduler, isOptional, isBody, isAsync, isFlowable, isSingle, isMaybe, false
        )
    }

    companion object {
        /**
         * Returns an instance which creates synchronous observables that do not operate on any scheduler
         * by default.
         */
        fun create(): RxJava2CallAdapterFactory {
            return RxJava2CallAdapterFactory(null, false)
        }

        /**
         * Returns an instance which creates asynchronous observables. Applying
         * [Observable.subscribeOn] has no effect on stream types created by this factory.
         */
        fun createAsync(): RxJava2CallAdapterFactory {
            return RxJava2CallAdapterFactory(null, true)
        }

        /**
         * Returns an instance which creates synchronous observables that
         * [subscribe on][Observable.subscribeOn] `scheduler` by default.
         */
        // Guarding public API nullability.
        fun createWithScheduler(scheduler: Scheduler?): RxJava2CallAdapterFactory {
            if (scheduler == null) throw NullPointerException("scheduler == null")
            return RxJava2CallAdapterFactory(scheduler, false)
        }
    }
}