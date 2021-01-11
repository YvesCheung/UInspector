package com.huya.mobile.uinspector.util

import android.view.View
import android.view.ViewParent

/**
 * @author YvesCheung
 * 2021/1/11
 */
/**
 * @return Find the DecorView of this [View]
 */
internal fun View.findRootParent(): View {
    var current: View = this
    var next: ViewParent? = current.parent
    while (next is View) {
        current = next
        next = current.parent
    }
    return current
}