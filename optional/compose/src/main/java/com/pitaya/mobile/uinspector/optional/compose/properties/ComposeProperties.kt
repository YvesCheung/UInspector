package com.pitaya.mobile.uinspector.optional.compose.properties

import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView

/**
 * @author YvesCheung
 * 2021/2/2
 */
class ComposeProperties(
    view: ComposeView,
    private val actual: LinkedHashMap<String, Any?> = LinkedHashMap()
) : Map<String, Any?> by actual {

    init {
        val factories = UInspector.plugins[ComposePropertiesParserFactory::class.java]
        val parsers = factories
            .flatMap { it.tryCreate(view) }
            .sortedByDescending { it.priority }
        for (parser in parsers) {
            parser.parse(actual)
        }
    }
}