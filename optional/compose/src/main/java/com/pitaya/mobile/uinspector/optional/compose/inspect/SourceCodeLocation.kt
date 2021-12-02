package com.pitaya.mobile.uinspector.optional.compose.inspect

import androidx.compose.ui.tooling.data.Group
import androidx.compose.ui.tooling.data.UiToolingDataApi

/**
 * @author YvesCheung
 * 2021/12/2
 */
data class SourceCodeLocation(val file: String, val line: Int, val offset: Int) {

    override fun toString(): String = "$file:$line"
}

@OptIn(UiToolingDataApi::class)
internal fun createCodeLocation(group: Group): SourceCodeLocation? {
    val currentLocation = group.location
    return if (!group.name.isNullOrBlank() &&
        currentLocation != null &&
        currentLocation.sourceFile !in frameworkWhiteList
    ) {
        SourceCodeLocation(
            currentLocation.sourceFile.orEmpty(),
            currentLocation.lineNumber,
            currentLocation.offset,
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