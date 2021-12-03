package com.pitaya.mobile.uinspector.optional.compose.properties

import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/12/3
 */
object IgnoreProperty : ComposePropertiesParser {

    override val priority: Int = -1

    override fun parse(@Output props: MutableMap<String, Any?>) {
        //Nothing output
    }
}