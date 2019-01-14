package com.yueban.splashyo.util.ext

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