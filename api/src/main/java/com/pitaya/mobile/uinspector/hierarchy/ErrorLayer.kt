package com.pitaya.mobile.uinspector.hierarchy

/**
 * @author YvesCheung
 * 2021/11/30
 */
class ErrorLayer(override val name: CharSequence) : Layer {

    override val id: CharSequence = "-1"

    override val parent: Layer? = null

    override val children: Sequence<Layer> = emptySequence()

    override val width: Int = 0

    override val height: Int = 0

    override fun getLocation(): IntArray = intArrayOf(0, 0)
}