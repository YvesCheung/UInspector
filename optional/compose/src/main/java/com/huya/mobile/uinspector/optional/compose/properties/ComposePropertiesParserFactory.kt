package com.huya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier
import com.huya.mobile.uinspector.plugins.UInspectorPlugin

/**
 * @author YvesCheung
 * 2021/2/2
 */
interface ComposePropertiesParserFactory : UInspectorPlugin {

    fun tryCreate(modifier: Modifier): ComposePropertiesParser?
}