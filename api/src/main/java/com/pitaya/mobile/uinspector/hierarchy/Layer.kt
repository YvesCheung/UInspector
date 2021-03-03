@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package com.pitaya.mobile.uinspector.hierarchy

import androidx.annotation.Size
import android.view.View

/**
 * It's the wrapper for [View] and Jetpack compose layout.
 *
 * @author YvesCheung
 * 2021/1/28
 */
interface Layer {

    val name: CharSequence

    val id: CharSequence?

    val parent: Layer?

    val children: Sequence<Layer>

    val width: Int

    val height: Int

    @Size(2)
    fun getLocation(): IntArray
}