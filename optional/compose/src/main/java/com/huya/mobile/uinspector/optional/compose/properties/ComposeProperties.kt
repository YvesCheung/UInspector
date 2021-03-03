package com.huya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.tooling.inspector.InspectorNode
import com.huya.mobile.uinspector.UInspector

/**
 * @author YvesCheung
 * 2021/2/2
 */
class ComposeProperties(
    info: InspectorNode,
    private val actual: LinkedHashMap<String, Any?> = LinkedHashMap()
) : Map<String, Any?> by actual {

    init {
        val factories = UInspector.plugins[ComposePropertiesParserFactory::class.java]
//        info.modifiers.forEach { modifier ->
//            for (factory in factories) {
//                val parser = factory.tryCreate(modifier)
//                if (parser != null) {
//                    parser.parse(actual)
//                    break
//                }
//            }
//        }
    }
}