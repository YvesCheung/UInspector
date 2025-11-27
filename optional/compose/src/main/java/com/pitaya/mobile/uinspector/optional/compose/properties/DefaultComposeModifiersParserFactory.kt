package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.semantics.SemanticsModifier
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView

/**
 * @author YvesCheung
 * 2021/2/2
 */
class DefaultComposeModifiersParserFactory(
    private val context: android.content.Context
) : ComposePropertiesParserFactory {

    override val uniqueKey: String = "Default"

    override fun tryCreate(view: ComposeView): List<ComposePropertiesParser> {
        return view.modifiers.mapNotNull { modifier ->
            when {
                modifier is SemanticsModifier -> null

                GraphicsLayerElementParser.accept(modifier) ->
                    GraphicsLayerElementParser(modifier)

                SimpleGraphicsLayerModifierParser.accept(modifier) ->
                    SimpleGraphicsLayerModifierParser(modifier)

                BlockGraphicsLayerModifierParser.accept(modifier) ->
                    BlockGraphicsLayerModifierParser(modifier)

                PaddingModifierParser.accept(modifier) -> null

                BorderModifierParser.accept(modifier) -> {
                    BorderModifierParser(modifier)
                }

                BackgroundModifierParser.accept(modifier) ->
                    BackgroundModifierParser(modifier)

                TextStringSimpleElementParser.accept(modifier) ->
                    TextStringSimpleElementParser(modifier)

                else -> InspectableModifierParser(modifier)
            }
        } + listOf(BasicParser(view), SemanticsModifierParser(context, view))
    }
}