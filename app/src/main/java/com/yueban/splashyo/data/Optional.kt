package com.yueban.splashyo.data

data class Optional<T>(private val value: T?, val isCache: Boolean = false) {
    val isNull: Boolean
        get() = this.value == null

    fun get(): T {
        if (value == null) {
            throw NoSuchElementException("No value present")
        }
        return value
    }

    fun getNullable(): T? {
        return value
    }

    companion object {
        fun <T> empty(isCache: Boolean = false): Optional<T> = Optional(null, isCache)
    }
}
