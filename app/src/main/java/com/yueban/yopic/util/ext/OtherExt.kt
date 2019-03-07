@file:Suppress("unused")

package com.yueban.yopic.util.ext

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yueban.yopic.util.displayMetrics

/**
 * @author yueban
 * @date 2019/1/14
 * @email fbzhh007@gmail.com
 */
fun SmartRefreshLayout.finishRefreshAndLoadMore(success: Boolean = true, hasMore: Boolean = true) {
    finishRefresh(0, success)
    finishLoadMore(0, success, !hasMore)
}

fun SmartRefreshLayout.autoAnimationOnly(refresh: Boolean = false, loadMore: Boolean = false) {
    if (refresh) {
        autoRefreshAnimationOnly()
    }
    if (loadMore) {
        autoLoadMoreAnimationOnly()
    }
}

fun Float.toDp(): Int = (this * displayMetrics.density).toInt()

fun Float.toPx(): Int = (this / displayMetrics.density).toInt()

fun Int.toDp(): Int = (this * displayMetrics.density).toInt()

fun Int.toPx(): Int = (this / displayMetrics.density).toInt()

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()