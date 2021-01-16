package com.huya.mobile.uinspector.impl.targets

import androidx.annotation.ColorInt
import com.github.lzyzsd.randomcolor.RandomColor
import com.github.lzyzsd.randomcolor.RandomColor.Color.*

/**
 * @author YvesCheung
 * 2021/1/16
 */
object ColorGenerator {

    private val base = arrayOf(
        RED, GREEN, BLUE, YELLOW
    )

    private var id = 0

    private var size = base.size

    private val random = RandomColor()

    @ColorInt
    fun next(): Int {
        id = ++id % size
        return random.randomColor(base[id])
    }
}