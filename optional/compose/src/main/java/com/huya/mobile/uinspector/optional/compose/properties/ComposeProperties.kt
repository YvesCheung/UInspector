package com.huya.mobile.uinspector.optional.compose.properties

import android.view.View
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.optional.compose.inspect.ComposeLayoutInfo

/**
 * @author YvesCheung
 * 2021/2/2
 */
class ComposeProperties(
    info: ComposeLayoutInfo,
    private val actual: LinkedHashMap<String, Any?> = LinkedHashMap()
) : Map<String, Any?> by actual {

    init {
        val factories = UInspector.plugins[ComposePropertiesParserFactory::class.java]
        info.modifiers.forEach { modifier ->
            for (factory in factories) {
                val parser = factory.tryCreate(modifier)
                if (parser != null) {
                    parser.parse(actual)
                    break
                }
            }
        }
    }
}