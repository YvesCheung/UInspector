package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.InspectableValue
import com.github.yvescheung.whisper.Output
import com.pitaya.mobile.uinspector.util.simpleName
/**
 * @author YvesCheung
 * 2021/2/2
 */
class UnknownModifierParser(val modifier: Modifier) : ComposePropertiesParser {

    override val priority: Int = -10

    override fun parse(@Output props: MutableMap<String, Any?>) {
        props[modifier.simpleName] = modifier.toString()
        if (modifier is InspectableValue) {
            modifier.inspectableElements.forEach {
                props[it.name] = it.value
            }
        }
    }
}