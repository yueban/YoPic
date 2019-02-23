package com.yueban.splashyo.util.concurrent

import io.reactivex.Scheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

object AppSchedulers {
    private val io: Scheduler = Schedulers.from(AppExecutors.io())
    private val singleton: Scheduler = Schedulers.from(AppExecutors.singleton())
    private val mainThread: Scheduler = Schedulers.from(AppExecutors.mainThread())

    fun io() = io
    fun singleton() = singleton
    fun mainThread() = mainThread

    init {
        RxJavaPlugins.setIoSchedulerHandler { io }
        RxJavaPlugins.setSingleSchedulerHandler { singleton }
    }
}