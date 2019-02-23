package com.yueban.splashyo.util.rxtransformer

import com.yueban.splashyo.data.Optional
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

class MarkAsCacheTransformer<T> : CombineTransformer<Optional<T>, Optional<T>> {
    override fun apply(upstream: Observable<Optional<T>>): ObservableSource<Optional<T>> =
        upstream.map {
            if (it.isNull) {
                Optional.empty(true)
            } else {
                Optional(it.get(), true)
            }
        }

    override fun apply(upstream: Flowable<Optional<T>>): Publisher<Optional<T>> =
        upstream.map {
            if (it.isNull) {
                Optional.empty(true)
            } else {
                Optional(it.get(), true)
            }
        }

    override fun apply(upstream: Single<Optional<T>>): SingleSource<Optional<T>> =
        upstream.map {
            if (it.isNull) {
                Optional.empty(true)
            } else {
                Optional(it.get(), true)
            }
        }

    override fun apply(upstream: Maybe<Optional<T>>): MaybeSource<Optional<T>> =
        upstream.map {
            if (it.isNull) {
                Optional.empty(true)
            } else {
                Optional(it.get(), true)
            }
        }

    override fun apply(upstream: Completable): CompletableSource = upstream
}