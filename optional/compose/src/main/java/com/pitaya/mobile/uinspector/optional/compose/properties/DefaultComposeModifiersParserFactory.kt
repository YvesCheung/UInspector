package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.semantics.SemanticsModifier
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView

/**
 * @author YvesCheung
 * 2021/2/2
 */
class DefaultComposeModifiersParserFactory : ComposePropertiesParserFactory {

    override val uniqueKey: String = "Default"

    override fun tryCreate(view: ComposeView): List<ComposePropertiesParser> {
        return view.modifiers.map { modifier ->
            when {
                modifier is SemanticsModifier ->
                    SemanticsModifierParser(modifier)
                SimpleGraphicsLayerModifierParser.accept(modifier) ->
                    SimpleGraphicsLayerModifierParser(modifier)
                BlockGraphicsLayerModifierParser.accept(modifier) ->
                    BlockGraphicsLayerModifierParser(modifier)
                PaddingModifierParser.accept(modifier)->
                    IgnoreProperty
                else ->
                    UnknownModifierParser(modifier)
            }
        } + BasicParser(view)
    }
}