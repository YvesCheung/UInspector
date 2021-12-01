package com.pitaya.mobile.uinspector.optional.compose.properties

import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/2/2
 */
interface ComposePropertiesParser {

    /**
     * The higher the priority, the earlier to parse
     */
    val priority: Int

    fun parse(@Output props: MutableMap<String, Any?>)
}