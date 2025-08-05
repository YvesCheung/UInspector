package com.pitaya.mobile.uinspector.optional.compose.inspect

import androidx.compose.ui.tooling.data.SourceLocation
import androidx.compose.ui.tooling.data.UiToolingDataApi

/**
 * @author YvesCheung
 * 2021/12/2
 */
data class SourceCodeLocation(val file: String, val line: Int, val offset: Int) {

    @OptIn(UiToolingDataApi::class)
    constructor(location: SourceLocation) :
        this(location.sourceFile.orEmpty(), location.lineNumber, location.offset)

    override fun toString(): String = "$file:$line"
}

@OptIn(UiToolingDataApi::class)
internal fun notInFramework(callChain: List<CallGroupInfo>): SourceCodeLocation? {
    for (call in callChain) {
        if (call.location == null) continue
        if (call.location.sourceFile in frameworkWhiteList) break
        return SourceCodeLocation(call.location)
    }
    return null
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