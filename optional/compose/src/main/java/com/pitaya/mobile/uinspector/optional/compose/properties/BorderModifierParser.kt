package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pitaya.mobile.uinspector.optional.compose.util.colorToString
import com.pitaya.mobile.uinspector.optional.compose.util.tryGetField
import com.pitaya.mobile.uinspector.util.canonicalName

/**
 * @author hautc11
 * 2025/11/20
 */
class BorderModifierParser(private val modifier: Modifier) : ComposePropertiesParser {

    override val priority: Int = 10


    override fun parse(props: MutableMap<String, Any?>) {
        val rawWidth = modifier.tryGetField<Any?>("width")
        val widthPx = rawWidth as? Float

        if (widthPx != null && widthPx > 0) {
            props["borderWidth"] = "${widthPx.toInt()}.dp"

            val brush = modifier.tryGetField<Any>("brush")
            if (brush != null) {
                val color = brush.tryGetField<Color>("value")
                if (color != null) {
                    props["borderColor"] = colorToString(color)
                }
            }
            val shape = modifier.tryGetField<Any>("shape")
            if (shape != null) {
                props["borderShape"] = shape
            }
        }
    }

    companion object {

        fun accept(modifier: Modifier): Boolean {
            return modifier.canonicalName.contains("Border")
        }
    }
}