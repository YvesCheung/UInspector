package com.pitaya.mobile.uinspector.optional.compose.inspect

import android.view.View
import androidx.compose.ui.tooling.inspector.InspectorNode
import androidx.compose.ui.tooling.inspector.LayoutInspectorTree

/**
 * Why [LayoutInspectorTree] not work?
 *
 * @see [https://github.com/square/radiography/blob/main/radiography/src/main/java/radiography/internal/ComposeLayoutInfo.kt]
 */
internal object ComposeInspector {

    fun tryGetLayoutInfos(composeView: View): Sequence<InspectorNode>? {
        return LayoutInspectorTree().convert(composeView).asSequence()
    }
}

/**
 * True if this view looks like the private view type that Compose uses to host compositions.
 * It does a fuzzy match to try to detect unsupported Compose versions, which will not be rendered
 * but will at least warn that the version is unsupported.
 */
internal val View.mightBeComposeView: Boolean
    get() = "AndroidComposeView" in this::class.java.name

///**
// * Information about a Compose `LayoutNode`, extracted from a [Group] tree via [Group.layoutInfos].
// *
// * This is a useful layer of indirection from directly handling Groups because it allows us to
// * define our own notion of what an atomic unit of "composable" is independently from how Compose
// * actually represents things under the hood. When this changes in some future dev version, we
// * only need to update the "parsing" logic in this file.
// * It's also helpful since we actually gather data from multiple Groups for a single LayoutInfo,
// * so parsing them ahead of time into these objects means the visitor can be stateless.
// */
//@UiToolingDataApi
//data class ComposeLayoutInfo(
//    val name: String,
//    val bounds: IntBounds,
//    val modifiers: List<Modifier>,
//    val children: Sequence<ComposeLayoutInfo>,
//    val source: SourceLocation?,
//    val view: View?,
//    val isSubcomposition: Boolean = false
//) {
//
//    override fun hashCode(): Int = Objects.hash(name, bounds, view, isSubcomposition)
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is ComposeLayoutInfo) return false
//        if (name != other.name) return false
//        if (bounds != other.bounds) return false
//        if (view != other.view) return false
//        if (isSubcomposition != other.isSubcomposition) return false
//        return true
//    }
//}