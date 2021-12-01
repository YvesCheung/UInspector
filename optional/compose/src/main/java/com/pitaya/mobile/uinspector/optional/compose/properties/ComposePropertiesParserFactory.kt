package com.pitaya.mobile.uinspector.optional.compose.properties

import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * @author YvesCheung
 * 2021/2/2
 */
interface ComposePropertiesParserFactory : UInspectorPlugin {

    fun tryCreate(view: ComposeView): List<ComposePropertiesParser>
}