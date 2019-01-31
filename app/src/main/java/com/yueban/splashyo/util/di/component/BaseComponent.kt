package com.yueban.splashyo.util.di.component

import com.squareup.moshi.Moshi
import com.yueban.splashyo.util.AppExecutors
import com.yueban.splashyo.util.di.module.BaseModule
import dagger.Component
import javax.inject.Singleton

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
@Singleton
@Component(modules = [BaseModule::class])
interface BaseComponent {
    fun appExecutors(): AppExecutors

    fun moshi(): Moshi

    companion object {
        @Volatile
        private var INSTANCE: BaseComponent? = null

        fun getInstance(): BaseComponent {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildBaseComponent().also { INSTANCE = it }
            }
        }

        private fun buildBaseComponent(): BaseComponent {
            return DaggerBaseComponent.create()
        }
    }
}