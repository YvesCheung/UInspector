package com.huya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.InspectableValue
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/2/2
 */
class DefaultComposePropertiesParser(val modifier: Modifier) : ComposePropertiesParser {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        props[modifier::class.java.name] = modifier.toString()
        if (modifier is InspectableValue) {
            modifier.inspectableElements.forEach {
                props[it.name] = it.value
            }
        }
    }
}