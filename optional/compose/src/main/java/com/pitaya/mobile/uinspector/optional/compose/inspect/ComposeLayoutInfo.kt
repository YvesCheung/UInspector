@file:OptIn(UiToolingDataApi::class)

package com.pitaya.mobile.uinspector.optional.compose.inspect

import android.util.Log
import android.view.ViewGroup
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.ui.node.Ref
import androidx.compose.ui.tooling.data.*
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.ComposeView
import com.pitaya.mobile.uinspector.optional.compose.hirarchy.SubComposition

/**
 * Recursively parses [Layer]s from a [Group]. Groups form a tree and can contain different
 * type of nodes which represent function calls, arbitrary data stored directly in the slot table,
 * or just subtrees.
 *
 * This function walks the tree and collects only Groups which represent emitted values
 * ([NodeGroup]s). These either represent `LayoutNode`s (Compose's internal primitive for layout
 * algorithms) or classic Android views that the composition emitted. This function collapses all
 * the groups in between each of these nodes, but uses the top-most Group under the previous node
 * to derive the "name" of the [Layer]. The other [Layer] properties come directly off
 * [NodeGroup] values.
 */
fun parseGroupToLayer(
    group: Group,
    parent: Layer,
    parentName: String = "",
    codeLocation: SourceCodeLocation? = null
): Sequence<Layer> {
    return sequence {
        val name = parentName.ifBlank { group.name }.orEmpty()

        // Certain composables produce an internal structure that is hard to read if we report it exactly.
        // Instead, we use heuristics to recognize subtrees that match certain expected structures and
        // aggregate them somewhat before reporting.
        val subComposition = group.tryParseSubcomposition(parent, name)
        if (subComposition != null) {
            yieldAll(subComposition)
            return@sequence
        }

        val androidView = group.tryParseAndroidView(parent)
        if (androidView != null) {
            yieldAll(androidView)
            return@sequence
        }

        // This is an intermediate group that doesn't represent a LayoutNode, so we flatten by just
        // reporting its children without reporting a new subtree.
        if (group !is NodeGroup) {
            val location = codeLocation ?: createCodeLocation(group)
            yieldAll(
                group.children.asSequence()
                    .flatMap { parseGroupToLayer(it, parent, name, location) } +
                    group.subComposedChildren() +
                    group.androidViewChildren()
            )
            return@sequence
        }

        yield(ComposeView(name, group, codeLocation, parent))
    }
}

/**
 * Look for any `CompositionContext`s stored in this group. These will be rolled up into the
 * `SubcomposeLayout` if present, otherwise they will just be shown as regular children.
 * The compositionData val is marked as internal, and not intended for public consumption.
 * The returned [SubcompositionInfo]s should be collated by [tryParseSubcomposition].
 */
@OptIn(InternalComposeApi::class)
internal fun Group.subComposedChildren(): Sequence<SubComposition> =
    getCompositionContexts()
        .flatMap { it.tryGetComposers().asSequence() }
        .map { subcomposer ->
            SubComposition(
                id = position,
                bounds = box,
                composer = subcomposer
            )
        }

/**
 * The `AndroidView` composable remembers a [Ref] to a special internal subclass of [ViewGroup] that
 * manages the wiring between the hosting android view and the child view. This function looks for
 * refs to views and returns them as [AndroidViewInfo]s to be collated with [tryParseAndroidView].
 *
 * Note that [Ref] is a public type – any third-party composable could also remember a ref to a
 * view, and it would be reported by this function. That would almost certainly be a code smell for
 * a number of reasons though, so we don't try to ignore those cases.
 */
internal fun Group.androidViewChildren(): List<AndroidView> =
    data.mapNotNull { datum ->
        (datum as? Ref<*>)
            ?.value
            // The concrete type is actually an internal ViewGroup subclass that has all the wiring, but
            // ultimately it's still just a ViewGroup so this simple check works.
            ?.let { it as? ViewGroup }
            ?.let(::AndroidView)
    }

/**
 * SubcomposeLayouts need to be handled specially, because all their subcompositions are always
 * logical children of their single LayoutNode. In order to render them so that the rendering
 * actually matches that logical structure, we need to reorganize the subtree a bit so
 * subcompositions are children of the layout node and not siblings of it.
 *
 * Note that there's no sure-fire way to actually detect a SubcomposeLayout. The best we can do is
 * use a heuristic. If any part of the heuristics don't match, then we fall back to treating the
 * group like any other.
 *
 * The heuristic we use is:
 * - Name of the group is "SubcomposeLayout".
 * - Has one or more subcompositions under it.
 * - Has exactly one LayoutNode child.
 * - That LayoutNode has no children of its own.
 */
private fun Group.tryParseSubcomposition(
    parent: Layer,
    name: String
): Sequence<Layer>? {
    if (this.name != "SubcomposeLayout") return null

    val (subCompositions: List<SubComposition>, regularChildren: List<Layer>) =
        children.asSequence()
            .flatMap { group -> parseGroupToLayer(group, parent) }
            .partition { it is SubComposition }
            .let {
                // There's no type-safe partition operator so we just cast.
                @Suppress("UNCHECKED_CAST")
                it as Pair<List<SubComposition>, List<Layer>>
            }

    if (subCompositions.isEmpty()) return null
    if (regularChildren.size != 1) return null

    val mainNode: Layer = regularChildren.single()
    if (mainNode !is ComposeView) return null
    if (!mainNode.children.isEmpty()) return null

    // We can be pretty confident at this point that this is an actual SubcomposeLayout, so
    // expose its layout node as the parent of all its subcompositions.
    val subcompositionName = "$name(SubComposition)"
    subCompositions.forEach { subComposition ->
        subComposition.name = subcompositionName
        subComposition.parent = mainNode
    }
    mainNode.parent = parent
    mainNode.logicalChildren = subCompositions.asSequence()
    return sequenceOf(mainNode)
}

/**
 * The AndroidView composable also needs to be special-cased. The actual android view is stored
 * in a Ref deep inside the hierarchy somewhere, but we want to expose it as the immediate child
 * of nearest common parent node that contains both the android view and the LayoutNode that is
 * used as a proxy to measure and lay it out in the composable.
 *
 * We can't rely on just the composable name, since any composable could be called "AndroidView",
 * so if any of the subtree parsing fails to match our expectations, we fallback to treating it
 * like any other group. Note that this heuristic isn't as strict as the subcomposition one, since
 * there's only one way to get an android view into a composition, so we can rely more heavily on
 * the presence of an actual android view. We still require there to be only one LayoutNode child,
 * otherwise it would be ambiguous which node we should report as the parent of the view.
 * We also require the common parent to be a CallGroup, since that is a valid assumption as of the
 * time of this writing and it saves us the additional logic of having to decide whether to return
 * this or the mainNode as the root of the subtree if this is a NodeGroup for some reason.
 *
 * Note that while this looks very similar to the [tryParseSubcomposition], that is probably
 * mostly coincidental, so it's probably not a good idea to factor out any abstractions. Since
 * they both rely on internal-only implementation details of how the Compose runtime happens to
 * work, either of them could change independently in the future, and it will be easier to update
 * the logic of both if that happens if they're completely independent.
 */
private fun Group.tryParseAndroidView(parent: Layer): Sequence<Layer>? {
    if (this.name != "AndroidView") return null
    if (this !is CallGroup) return null

    val (androidViews, regularChildren) =
        children.asSequence()
            .flatMap { parseGroupToLayer(it, parent) }
            .partition { it is AndroidView }
            .let {
                // There's no type-safe partition operator so we just cast.
                @Suppress("UNCHECKED_CAST")
                it as Pair<List<AndroidView>, List<Layer>>
            }

    if (androidViews.isEmpty()) return null
    if (regularChildren.size != 1) return null

    val mainNode = regularChildren.single()
    if (mainNode !is ComposeView) return null

    // We can be pretty confident at this point that this is an actual AndroidView composable,
    // so expose its layout node as the parent of its actual view.
    mainNode.parent = parent
    mainNode.logicalChildren = androidViews.asSequence()
    return sequenceOf(mainNode)
}

private fun Sequence<*>.isEmpty(): Boolean = !iterator().hasNext()
