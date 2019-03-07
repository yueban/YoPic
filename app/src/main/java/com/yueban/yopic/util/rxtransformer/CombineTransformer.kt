package com.yueban.yopic.util.rxtransformer

import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.MaybeTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

interface CombineTransformer<T, R> : ObservableTransformer<T, R>, FlowableTransformer<T, R>, SingleTransformer<T, R>,
    MaybeTransformer<T, R>, CompletableTransformer