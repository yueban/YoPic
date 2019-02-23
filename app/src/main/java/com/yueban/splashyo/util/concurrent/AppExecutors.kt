package com.yueban.splashyo.util.concurrent

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {
    private val io: Executor = Executors.newFixedThreadPool(3)
    private val singleton: Executor = Executors.newSingleThreadExecutor()
    private val mainThread: Executor = MainThreadExecutor()

    fun io() = io
    fun singleton() = singleton
    fun mainThread() = mainThread

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }
    }
}
