package com.yueban.splashyo.util.rxtransformer

import androidx.room.EmptyResultSetException
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

class RoomOptionalTransformer<T> private constructor() : CombineTransformer<T, Optional<T>> {
    override fun apply(upstream: Observable<T>): ObservableSource<Optional<T>> =
        upstream.map {
            Optional(it)
        }.onErrorReturn { t ->
            if (t is EmptyResultSetException) {
                Optional.empty()
            } else {
                throw t
            }
        }

    override fun apply(upstream: Flowable<T>): Publisher<Optional<T>> =
        upstream.map {
            Optional(it)
        }.onErrorReturn { t ->
            if (t is EmptyResultSetException) {
                Optional.empty()
            } else {
                throw t
            }
        }

    override fun apply(upstream: Single<T>): SingleSource<Optional<T>> =
        upstream.map {
            Optional(it)
        }.onErrorReturn { t ->
            if (t is EmptyResultSetException) {
                Optional.empty()
            } else {
                throw t
            }
        }

    override fun apply(upstream: Maybe<T>): MaybeSource<Optional<T>> =
        upstream.map {
            Optional(it)
        }.onErrorReturn { t ->
            if (t is EmptyResultSetException) {
                Optional.empty()
            } else {
                throw t
            }
        }

    override fun apply(upstream: Completable): CompletableSource = upstream

    companion object {
        fun <T> create() = RoomOptionalTransformer<T>()
    }
}
