package com.yueban.yopic.util.rxtransformer

import com.yueban.yopic.util.concurrent.AppSchedulers
import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import org.reactivestreams.Publisher

class AsyncScheduler<T> private constructor() : CombineTransformer<T, T> {
    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream
            .subscribeOn(AppSchedulers.io())
            .observeOn(AppSchedulers.mainThread())

    override fun apply(upstream: Flowable<T>): Publisher<T> =
        upstream
            .subscribeOn(AppSchedulers.io())
            .observeOn(AppSchedulers.mainThread())

    override fun apply(upstream: Single<T>): SingleSource<T> = upstream
        .subscribeOn(AppSchedulers.io())
        .observeOn(AppSchedulers.mainThread())

    override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream
        .subscribeOn(AppSchedulers.io())
        .observeOn(AppSchedulers.mainThread())

    override fun apply(upstream: Completable): CompletableSource = upstream
        .subscribeOn(AppSchedulers.io())
        .observeOn(AppSchedulers.mainThread())

    companion object {
        fun <T> create() = AsyncScheduler<T>()
    }
}