package com.pitaya.mobile.uinspector.optional.compose.properties

import android.content.Context
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.semantics.SemanticsActions.GetTextLayoutResult
import androidx.compose.ui.semantics.SemanticsConfiguration
import androidx.compose.ui.semantics.SemanticsProperties.CollectionInfo
import androidx.compose.ui.semantics.SemanticsProperties.CollectionItemInfo
import androidx.compose.ui.semantics.SemanticsProperties.ContentDescription
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.semantics.SemanticsProperties.Error
import androidx.compose.ui.semantics.SemanticsProperties.Focused
import androidx.compose.ui.semantics.SemanticsProperties.HorizontalScrollAxisRange
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.semantics.SemanticsProperties.PaneTitle
import androidx.compose.ui.semantics.SemanticsProperties.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties.Role
import androidx.compose.ui.semantics.SemanticsProperties.Selected
import androidx.compose.ui.semantics.SemanticsProperties.StateDescription
import androidx.compose.ui.semantics.SemanticsProperties.TestTag
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.semantics.SemanticsProperties.TextSelectionRange
import androidx.compose.ui.semantics.SemanticsProperties.ToggleableState
import androidx.compose.ui.semantics.SemanticsProperties.VerticalScrollAxisRange
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.unit.isSpecified
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.optional.compose.util.colorToString
import com.pitaya.mobile.uinspector.optional.compose.util.tryGetField
import com.pitaya.mobile.uinspector.util.Output
import com.pitaya.mobile.uinspector.util.quote

/**
 * @author YvesCheung & hautc11
 * 2021/12/1
 */
class SemanticsModifierParser(
    private val context: Context,
    val view: ComposeView
) : ComposePropertiesParser {

    override val priority: Int = 10000

    override fun parse(@Output props: MutableMap<String, Any?>) {
        val configs = view.semanticsConfigurations

        fun <T : Any> parseSemanticsProperty(
            property: SemanticsPropertyKey<T>,
            inspect: (T?) -> CharSequence? = { if (it is CharSequence) it.quote() else it?.toString() }
        ) {
            var value: T? = null
            for (config in configs) {
                value = config.getOrNull(property)
                if (value != null) break
            }
            val inspectCharSequence = inspect(value)
            if (!inspectCharSequence.isNullOrBlank()) {
                props[property.name] = inspectCharSequence
            }
        }

        parseSemanticsProperty(Role)

        parseSemanticsProperty(Text) {
            it?.firstOrNull()?.let { annotatedString ->
                props["text"] = annotatedString.text.quote()
            }
            null
        }

        parseTextLayoutResult(props, configs)

        parseSemanticsProperty(EditableText)

        parseSemanticsProperty(TestTag)

        parseSemanticsProperty(ContentDescription) { it?.joinToString() }

        parseSemanticsProperty(StateDescription)

        parseSemanticsProperty(ProgressBarRangeInfo)

        parseSemanticsProperty(PaneTitle)

        parseSemanticsProperty(CollectionInfo) { collectionInfo ->
            collectionInfo?.let { "CollectionInfo(rowCount=${it.rowCount}, columnCount=${it.columnCount})" }
        }

        parseSemanticsProperty(CollectionItemInfo) { collectionItemInfo ->
            collectionItemInfo?.let {
                "CollectionItemInfo(rowIndex=${it.rowIndex}, rowSpan=${it.rowSpan}, " +
                    "columnIndex=${it.columnIndex}, columnSpan=${it.columnSpan})"
            }
        }

        parseSemanticsProperty(Focused)

        parseSemanticsProperty(HorizontalScrollAxisRange) { range ->
            range?.let { "ScrollAxisRange(${it.value},${it.maxValue})" }
        }

        parseSemanticsProperty(VerticalScrollAxisRange) { range ->
            range?.let { "ScrollAxisRange(${it.value},${it.maxValue})" }
        }

        parseSemanticsProperty(TextSelectionRange)

        parseSemanticsProperty(ImeAction)

        parseSemanticsProperty(Selected)

        parseSemanticsProperty(ToggleableState)

        parseSemanticsProperty(Error)
    }

    private fun parseTextLayoutResult(
        props: MutableMap<String, Any?>,
        configs: List<SemanticsConfiguration>
    ) {
        val getTextLayoutResult = configs.firstNotNullOfOrNull { it.getOrNull(GetTextLayoutResult) }
        if (getTextLayoutResult?.action != null) {
            val layoutResults = mutableListOf<androidx.compose.ui.text.TextLayoutResult>()
            try {
                val success = getTextLayoutResult.action?.invoke(layoutResults)
                if (success == true && layoutResults.isNotEmpty()) {
                    val textLayoutResult = layoutResults.first()
                    val style = textLayoutResult.layoutInput.style

                    if (style.fontSize.isSpecified) {
                        props["fontSize"] = style.fontSize.toString()
                    }
                    style.fontWeight?.let { props["fontWeight"] = it.weight }
                    style.fontFamily?.let {
                        if (it.javaClass.simpleName == "FontListFontFamily") {
                            val fonts = it.tryGetField<List<Any>>("fonts")
                            if (fonts != null) {
                                val fontNames = fonts.map { font ->
                                    if (font.javaClass.simpleName == "ResourceFont") {
                                        val resId = font.tryGetField<Int>("resId")
                                        if (resId != null) {
                                            try {
                                                "R.font.${context.resources.getResourceEntryName(resId)}"
                                            } catch (_: Exception) {
                                                "resId=$resId"
                                            }
                                        } else {
                                            "ResourceFont"
                                        }
                                    } else {
                                        font.javaClass.simpleName
                                    }
                                }
                                props["fontFamily"] = fontNames.joinToString(",\n")
                            } else {
                                props["fontFamily"] = it.toString()
                            }
                        } else {
                            props["fontFamily"] = it.toString()
                        }
                    }
                    style.fontStyle?.let { props["fontStyle"] = it.toString() }
                    if (style.color.isSpecified) {
                        props["textColor"] = colorToString(style.color)
                    }
                }
            } catch (e: Exception) {
                props["GetTextLayoutResult_Error"] = e.message ?: "Unknown error"
            }
        }
    }
}