package com.yueban.splashyo.util.ext

import android.content.res.Resources
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * @author yueban
 * @date 2019/1/14
 * @email fbzhh007@gmail.com
 */
fun SmartRefreshLayout.finishRefreshAndLoadMore() {
    finishRefresh()
    finishLoadMore()
}

fun SmartRefreshLayout.autoAnimationOnly(refresh: Boolean = false, loadMore: Boolean = false) {
    if (refresh) {
        autoRefreshAnimationOnly()
    }
    if (loadMore) {
        autoLoadMoreAnimationOnly()
    }
}

fun Float.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Float.toPx(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()