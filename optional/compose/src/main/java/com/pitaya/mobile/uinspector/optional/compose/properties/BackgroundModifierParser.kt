package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import com.pitaya.mobile.uinspector.optional.compose.util.colorToString
import com.pitaya.mobile.uinspector.optional.compose.util.simpleName
import com.pitaya.mobile.uinspector.optional.compose.util.tryGetField
import com.pitaya.mobile.uinspector.util.canonicalName

/**
 * @author hautc11
 * 2025/11/20
 */
class BackgroundModifierParser(private val modifier: Modifier) : ComposePropertiesParser {

    override val priority: Int = 10

    override fun parse(props: MutableMap<String, Any?>) {
        val color = modifier.tryGetField<Any>("color")
        if (color != null) {
            props["backgroundColor"] = colorToString(color)
        } else {
            val brush = modifier.tryGetField<Any>("brush")
            if (brush != null) {
                props["backgroundBrush"] = brush.simpleName
            }
        }
        val shape = modifier.tryGetField<Any>("shape")
        if (shape != null) {
            props["backgroundShape"] = shape.toString()
        }
    }

    companion object {

        fun accept(modifier: Modifier): Boolean {
            return modifier.canonicalName.contains("Background")
        }
    }
}
