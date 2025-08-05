package com.pitaya.mobile.uinspector.optional.compose.inspect

import androidx.compose.ui.tooling.data.SourceLocation
import androidx.compose.ui.tooling.data.UiToolingDataApi

/**
 * @author YvesCheung
 * 2021/12/2
 */
data class SourceCodeLocation(val file: String, val line: Int, val offset: Int) {

    override fun toString(): String = "$file:$line"
}

@OptIn(UiToolingDataApi::class)
internal fun createCodeLocation(name: CharSequence, sourceLocation: SourceLocation?): SourceCodeLocation? {
    return if (name.isNotBlank() &&
        sourceLocation != null &&
        sourceLocation.sourceFile !in frameworkWhiteList
    ) {
        SourceCodeLocation(
            sourceLocation.sourceFile.orEmpty(),
            sourceLocation.lineNumber,
            sourceLocation.offset,
        )
    } else {
        null
    }
}

/**
 * No need to locate the source code which is in Compose framework
 */
private val frameworkWhiteList = setOf(
    "Wrapper.android.kt",
    "Composer.kt",
    "AndroidCompositionLocals.android.kt",
    "Effects.kt",
    "CompositionLocal.kt",
    "CompositionLocals.kt",
    "SubcomposeLayout.kt",
    "ComposeView.android.kt",
    "AndroidDialog.android.kt",
)