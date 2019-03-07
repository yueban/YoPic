package com.yueban.yopic.util.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

/**
 * @author yueban
 * @date 2019/1/23
 * @email fbzhh007@gmail.com
 */
abstract class FABBaseBehavior<T : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<T>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: T,
        dependency: View
    ): Boolean {
        val translationY = Math.min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }
}