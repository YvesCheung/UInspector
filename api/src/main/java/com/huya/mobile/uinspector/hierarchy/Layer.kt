@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package com.huya.mobile.uinspector.hierarchy

/**
 * @author YvesCheung
 * 2021/1/28
 */
interface Layer {

    val name: CharSequence

    val parent: Layer?

    val children: Sequence<Layer>
}