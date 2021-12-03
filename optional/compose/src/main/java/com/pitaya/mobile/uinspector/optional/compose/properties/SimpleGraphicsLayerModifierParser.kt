package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope

/**
 * @see androidx.compose.ui.graphics.SimpleGraphicsLayerModifier
 *
 * @author YvesCheung
 * 2021/12/3
 */
class SimpleGraphicsLayerModifierParser(val modifier: Modifier) : GraphicsLayerModifier() {

    @Suppress("UNCHECKED_CAST")
    override fun parseConfig(): GraphicsConfig? {
        return try {
            val layerBlock: GraphicsLayerScope.() -> Unit =
                layerBlockField.get(modifier) as GraphicsLayerScope.() -> Unit
            return GraphicsConfig().also(layerBlock)
        } catch (e: Throwable) {
            null
        }
    }

    companion object {

        private val modifierClass by lazy(LazyThreadSafetyMode.NONE) {
            Class.forName("androidx.compose.ui.graphics.SimpleGraphicsLayerModifier")
        }

        private val layerBlockField by lazy(LazyThreadSafetyMode.NONE) {
            val field = modifierClass.getDeclaredField("layerBlock")
            field.isAccessible = true
            field
        }

        private var ClassNotFound = false

        fun accept(modifier: Modifier): Boolean {
            return if (!ClassNotFound) {
                try {
                    modifierClass.isInstance(modifier)
                } catch (e: Throwable) {
                    ClassNotFound = true
                    false
                }
            } else {
                false
            }
        }
    }
}