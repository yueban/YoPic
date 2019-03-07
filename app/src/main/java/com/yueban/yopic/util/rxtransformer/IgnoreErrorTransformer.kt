package com.yueban.yopic.util.rxtransformer

import com.elvishew.xlog.XLog
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.internal.operators.completable.CompletableEmpty
import org.reactivestreams.Publisher

class IgnoreErrorTransformer<T>
private constructor(private val printError: Boolean) : CombineTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.doOnError { throwable ->
            if (printError) {
                XLog.e(throwable)
            }
        }.onErrorResumeNext(Observable.empty())
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.doOnError { throwable ->
            if (printError) {
                XLog.e(throwable)
            }
        }.onErrorResumeNext(Flowable.empty())
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.doOnError { throwable ->
            if (printError) {
                XLog.e(throwable)
            }
        }.onErrorResumeNext(Maybe.empty<T>().toSingle())
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.doOnError { throwable ->
            if (printError) {
                XLog.e(throwable)
            }
        }.onErrorResumeNext(Maybe.empty())
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.doOnError { throwable ->
            if (printError) {
                XLog.e(throwable)
            }
        }.onErrorResumeNext { CompletableEmpty.INSTANCE }
    }

    companion object {
        fun <T> create(printError: Boolean = true) = IgnoreErrorTransformer<T>(printError)
    }
}