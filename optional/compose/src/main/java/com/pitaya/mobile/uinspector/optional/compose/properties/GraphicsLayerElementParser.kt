package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.node.ModifierNodeElement
import com.pitaya.mobile.uinspector.optional.compose.properties.SimpleGraphicsLayerModifierParser.Companion.layerBlockField

/**
 * @see androidx.compose.ui.graphics.GraphicsLayerElement
 *
 * @author YvesCheung
 * 2025/8/5
 */
class GraphicsLayerElementParser(val modifier: Modifier) : GraphicsLayerModifier() {

    @Suppress("UNCHECKED_CAST")
    override fun parseConfig(): GraphicsConfig? {
        return try {
            /**
             * [androidx.compose.ui.graphics.SimpleGraphicsLayerModifier]
             */
            val simpleGraphicsNode = (modifier as ModifierNodeElement<*>).create()
            val layerBlock: GraphicsLayerScope.() -> Unit =
                layerBlockField.get(simpleGraphicsNode) as GraphicsLayerScope.() -> Unit
            return GraphicsConfig().also(layerBlock)
        } catch (e: Throwable) {
            null
        }
    }

    companion object {

        private val elementClass by lazy(LazyThreadSafetyMode.NONE) {
            Class.forName("androidx.compose.ui.graphics.GraphicsLayerElement")
        }

        private var ClassNotFound = false

        fun accept(modifier: Modifier): Boolean {
            return if (!ClassNotFound) {
                try {
                    elementClass.isInstance(modifier)
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