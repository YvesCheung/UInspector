package com.huya.mobile.uinspector.hierarchy

import android.view.View
import android.view.ViewParent

/**
 * @author YvesCheung
 * 2021/1/4
 */
object ViewHierarchy {

    fun get(target: View): List<View> {
        val result = mutableListOf(target)
        var parent: ViewParent? = target.parent
        while (parent is View) {
            result.add(parent)
            parent = target.parent
        }
        return result
    }
}