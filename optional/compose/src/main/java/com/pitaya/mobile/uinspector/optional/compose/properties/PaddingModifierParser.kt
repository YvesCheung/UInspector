package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.util.canonicalName

/**
 *
 * @see androidx.compose.foundation.layout.PaddingModifier
 * @see androidx.compose.foundation.layout.PaddingElement
 *
 * @author YvesCheung
 * 2021/12/1
 */
object PaddingModifierParser {

    fun accept(modifier: Modifier): Boolean {
        return isPaddingElement(modifier) || isPaddingModifier(modifier)
    }

    /**
     * compose > 1.6.0
     */
    private fun isPaddingElement(modifier: Modifier): Boolean {
        return modifier.canonicalName.contains("PaddingElement") &&
            PaddingElementClass.isInstance(modifier)
    }

    /**
     * compose < 1.6.0
     */
    private fun isPaddingModifier(modifier: Modifier): Boolean {
        return modifier.canonicalName.contains("PaddingModifier") &&
            PaddingModifierClass.isInstance(modifier)
    }

    fun parse(view: ComposeView): PaddingModifier {
        val element = view.modifiers.find(::isPaddingElement)
        if (element != null) {
            return PaddingModifier(
                start = startField2.getFloat(element),
                top = topField2.getFloat(element),
                end = endField2.getFloat(element),
                bottom = bottomField2.getFloat(element),
                rtlAware = rtlAwareField2.getBoolean(element)
            )
        } else {
            val modifier = view.modifiers.find(::isPaddingModifier)
            if (modifier != null) {
                return PaddingModifier(
                    start = startField.getFloat(modifier),
                    top = topField.getFloat(modifier),
                    end = endField.getFloat(modifier),
                    bottom = bottomField.getFloat(modifier),
                    rtlAware = rtlAwareField.getBoolean(modifier)
                )
            }
        }
        return PaddingModifier()
    }

    /**
     * value in dp
     */
    data class PaddingModifier(
        val start: Float = 0f,
        val top: Float = 0f,
        val end: Float = 0f,
        val bottom: Float = 0f,
        val rtlAware: Boolean = false
    ) {
        val left: Float = if (rtlAware) end else start
        val right: Float = if (rtlAware) start else end
    }

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

    private val PaddingElementClass by lazy(LazyThreadSafetyMode.NONE) {
        Class.forName("androidx.compose.foundation.layout.PaddingElement")
    }

    private val startField2 by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingElementClass.getDeclaredField("start")
        f.isAccessible = true
        f
    }

    private val topField2 by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingElementClass.getDeclaredField("top")
        f.isAccessible = true
        f
    }

    private val endField2 by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingElementClass.getDeclaredField("end")
        f.isAccessible = true
        f
    }

    private val bottomField2 by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingElementClass.getDeclaredField("bottom")
        f.isAccessible = true
        f
    }

    private val rtlAwareField2 by lazy(LazyThreadSafetyMode.NONE) {
        val f = PaddingElementClass.getDeclaredField("rtlAware")
        f.isAccessible = true
        f
    }
}