package com.pitaya.mobile.uinspector.hierarchy

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
            parent = (parent as View).parent
        }
        result.reverse()
        return result
    }

    fun get(target: Layer): List<Layer> {
        val result = mutableListOf(target)
        var parent: Layer? = target.parent
        while (parent != null) {
            result.add(parent)
            parent = parent.parent
        }
        result.reverse()
        return result
    }
}