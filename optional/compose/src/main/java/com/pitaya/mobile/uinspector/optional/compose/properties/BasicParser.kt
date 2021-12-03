package com.pitaya.mobile.uinspector.optional.compose.properties

import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView

/**
 * @author YvesCheung
 * 2021/12/3
 */
class BasicParser(val view: ComposeView) : ComposePropertiesParser {

    override val priority: Int = 11000

    override fun parse(props: MutableMap<String, Any?>) {
        props["name"] = view.name
        props["source"] = view.sourceCode
    }
}