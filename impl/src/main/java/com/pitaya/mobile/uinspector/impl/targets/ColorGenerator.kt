package com.pitaya.mobile.uinspector.impl.targets

import android.util.LruCache
import androidx.annotation.ColorInt
import com.pitaya.mobile.uinspector.impl.targets.lzyzsd.RandomColor
import com.pitaya.mobile.uinspector.impl.targets.lzyzsd.RandomColor.Color.*

/**
 * @author YvesCheung
 * 2021/1/16
 */
object ColorGenerator {

    private val cacheColor = LruCache<Int, Int>(40)

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

    @ColorInt
    fun next(target: Any): Int {
        val cacheKey = target.hashCode()
        val cacheValue = cacheColor[cacheKey]
        if (cacheValue != null) return cacheValue

        val color = next()
        cacheColor.put(cacheKey, color)
        return color
    }
}