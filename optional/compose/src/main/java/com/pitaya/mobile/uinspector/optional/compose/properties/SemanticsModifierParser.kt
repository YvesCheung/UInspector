package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.semantics.SemanticsModifier
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
import com.github.yvescheung.whisper.Output
import com.pitaya.mobile.uinspector.util.quote

/**
 * @author YvesCheung
 * 2021/12/1
 */
class SemanticsModifierParser(val modifier: SemanticsModifier) : ComposePropertiesParser {

    override val priority: Int = 10000

    override fun parse(@Output props: MutableMap<String, Any?>) {
        val config = modifier.semanticsConfiguration

        fun <T : Any> parseSemanticsProperty(
            property: SemanticsPropertyKey<T>,
            inspect: (T?) -> CharSequence? = { if (it is CharSequence) it.quote() else it?.toString() }
        ) {
            val inspectCharSequence = inspect(config.getOrNull(property))
            if (!inspectCharSequence.isNullOrBlank()) {
                props[property.name] = inspectCharSequence
            }
        }

        parseSemanticsProperty(Role)

        parseSemanticsProperty(Text) { stringList ->
            stringList?.joinToString { annotatedString -> "\"${annotatedString}\"" }
        }

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
}