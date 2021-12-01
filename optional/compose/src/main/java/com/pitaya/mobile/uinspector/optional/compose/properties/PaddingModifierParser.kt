package com.pitaya.mobile.uinspector.optional.compose.properties

import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.util.canonicalName

/**
 * @see androidx.compose.foundation.layout.PaddingModifier
 *
 * @author YvesCheung
 * 2021/12/1
 */
object PaddingModifierParser {

    fun parse(view: ComposeView): PaddingModifier {
        val modifier = view.modifiers.find {
            it.canonicalName.contains("PaddingModifier") &&
                PaddingModifierClass.isInstance(it)
        }
        if (modifier != null) {
            return PaddingModifier(
                start = startField.getFloat(modifier),
                top = topField.getFloat(modifier),
                end = endField.getFloat(modifier),
                bottom = bottomField.getFloat(modifier),
                rtlAware = rtlAwareField.getBoolean(modifier)
            )
        }
        return PaddingModifier()
    }

    data class PaddingModifier(
        val start: Float = 0f,
        val top: Float = 0f,
        val end: Float = 0f,
        val bottom: Float = 0f,
        val rtlAware: Boolean = false
    )

    private val PaddingModifierClass by lazy(LazyThreadSafetyMode.NONE) {
        Class.forName("androidx.compose.foundation.layout.PaddingModifier")
    }

    private val startField by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingModifierClass.getDeclaredField("start")
        f.isAccessible = true
        f
    }

    private val topField by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingModifierClass.getDeclaredField("top")
        f.isAccessible = true
        f
    }

    private val endField by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingModifierClass.getDeclaredField("end")
        f.isAccessible = true
        f
    }

    private val bottomField by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingModifierClass.getDeclaredField("bottom")
        f.isAccessible = true
        f
    }

    private val rtlAwareField by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingModifierClass.getDeclaredField("rtlAware")
        f.isAccessible = true
        f
    }
}