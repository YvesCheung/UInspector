package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.Modifier

/**
 * @author YvesCheung
 * 2021/2/2
 */
class DefaultComposePropertiesParserFactory : ComposePropertiesParserFactory {

    override val uniqueKey: String = "Default"

    override fun tryCreate(modifier: Modifier): ComposePropertiesParser =
        DefaultComposePropertiesParser(modifier)

}