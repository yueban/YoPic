package com.yueban.yopic.util.di.component

import com.squareup.moshi.Moshi
import com.yueban.yopic.util.moshi.ApplicationJsonAdapterFactory
import com.yueban.yopic.util.moshi.MoshiDateConverter
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * @author yueban
 * @date 2019/2/1
 * @email fbzhh007@gmail.com
 */
@Singleton
@Component
interface BaseComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun moshi(moshi: Moshi): Builder

        fun build(): BaseComponent
    }

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
            val moshi = Moshi.Builder()
                .add(MoshiDateConverter())
                .add(ApplicationJsonAdapterFactory.INSTANCE)
                .build()
            return DaggerBaseComponent.builder().moshi(moshi).build()
        }
    }
}