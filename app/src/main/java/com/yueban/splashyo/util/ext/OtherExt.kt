@file:Suppress("unused")

package com.yueban.splashyo.util.ext

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yueban.splashyo.util.displayMetrics

/**
 * @author yueban
 * @date 2019/1/14
 * @email fbzhh007@gmail.com
 */
fun SmartRefreshLayout.finishRefreshAndLoadMore(hasMore: Boolean = true) {
    finishRefresh()
    if (hasMore) {
        finishLoadMore()
    } else {
        finishLoadMoreWithNoMoreData()
    }
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