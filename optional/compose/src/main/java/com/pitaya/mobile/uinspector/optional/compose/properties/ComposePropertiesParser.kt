package com.pitaya.mobile.uinspector.optional.compose.properties

import com.pitaya.mobile.uinspector.util.Output

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