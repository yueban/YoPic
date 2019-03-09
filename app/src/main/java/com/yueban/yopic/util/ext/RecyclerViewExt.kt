package com.yueban.yopic.util.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author yueban
 * @date 2019/1/14
 * @email fbzhh007@gmail.com
 */
fun RecyclerView.scrollToTop(skipOnScrolling: Boolean = false): Boolean {
    if (isScrolling() && skipOnScrolling) {
        return false
    }

    stopScroll()
    return when (layoutManager) {
        is LinearLayoutManager -> {
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
            true
        }
        else -> false
    }
}

fun RecyclerView.isScrolling() = scrollState != RecyclerView.SCROLL_STATE_IDLE