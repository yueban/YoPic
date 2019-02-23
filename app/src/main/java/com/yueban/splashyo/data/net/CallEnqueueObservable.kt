package com.yueban.splashyo.data.net

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class CallEnqueueObservable<T>(private val originalCall: Call<T>) : Observable<Response<T>>() {

    override fun subscribeActual(observer: Observer<in Response<T>>) {
        // Since Call is a one-shot type, clone it for each new observer.
        val call = originalCall.clone()
        val callback = CallCallback(call, observer)
        observer.onSubscribe(callback)
        if (!callback.isDisposed) {
            call.enqueue(callback)
        }
    }

    private class CallCallback<T> internal constructor(
        private val call: Call<*>,
        private val observer: Observer<in Response<T>>
    ) :
        Disposable, Callback<T> {
        @Volatile
        private var disposed: Boolean = false
        internal var terminated = false

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (disposed) return

            try {
                observer.onNext(response)

                if (!disposed) {
                    terminated = true
                    observer.onComplete()
                }
            } catch (t: Throwable) {
                if (terminated) {
                    RxJavaPlugins.onError(t)
                } else if (!disposed) {
                    try {
                        observer.onError(t)
                    } catch (inner: Throwable) {
                        Exceptions.throwIfFatal(inner)
                        RxJavaPlugins.onError(CompositeException(t, inner))
                    }
                }
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) return

            try {
                observer.onError(t)
            } catch (inner: Throwable) {
                Exceptions.throwIfFatal(inner)
                RxJavaPlugins.onError(CompositeException(t, inner))
            }
        }

        override fun dispose() {
            disposed = true
            call.cancel()
        }

        override fun isDisposed(): Boolean {
            return disposed
        }
    }
}
