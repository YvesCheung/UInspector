package com.huya.mobile.uinspector.optional.compose.inspect

import android.util.SparseArray
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.Group
import androidx.compose.ui.tooling.NodeGroup
import androidx.compose.ui.tooling.asTree
import androidx.compose.ui.unit.IntBounds
import java.lang.reflect.Field
import androidx.compose.ui.tooling.inspector.LayoutInspectorTree
import java.util.*

/**
 * Why [LayoutInspectorTree] not work?
 *
 * @see [https://github.com/square/radiography/blob/main/radiography/src/main/java/radiography/internal/ComposeLayoutInfo.kt]
 */
@OptIn(InternalComposeApi::class)
internal object ComposeInspector {

    /**
     * Uses reflection to try to pull a `SlotTable` out of [composeView] and render it. If any of the
     * reflection fails, returns false.
     */
    fun tryGetLayoutInfos(composeView: View): Sequence<ComposeLayoutInfo>? {
        // Any of this reflection code can fail if running with an unsupported version of Compose.
        // Compose doesn't provide a public API for this (yet) because they don't want it to be used in
        // production.
        // See this slack thread: https://kotlinlang.slack.com/archives/CJLTWPH7S/p1596749016388100?thread_ts=1596749016.388100&cid=CJLTWPH7S
        val keyedTags = composeView.getKeyedTags()
        val composition = keyedTags.first { it is Composition } as Composition? ?: return null
        val composer = composition.unwrap()
            .getComposerOrNull() ?: return null

        // Composer and its slot table are finally public API again.
        // asTree is provided by the Compose Tooling library. It "reads" the slot table and parses it
        // into a tree of Group objects. This means we're technically traversing the composable tree
        // twice, so why not just read the slot table directly? As opaque as the Group API is, the actual
        // slot table API is quite complicated, and the actual format of the slot table (effectively an
        // array that stores a flattened version of a composition tree) is super low level. It's likely to
        // change a lot between compose versions, and keeping up with that with every two-week dev release
        // would be a lot of work. Additionally, a lot of the objects stored in the slot table are not
        // public (eg LayoutNode), so we'd need to use even more (brittle) reflection to do that parsing.
        // That said, once Compose is more stable, it might be worth it to read the slot table directly,
        // since then we could drop the requirement for the Tooling library to be on the classpath.
        return if (composer.compositionData.compositionGroups.iterator().hasNext())
            composer.compositionData.asTree().layoutInfos
        else null
    }

    private fun View.getKeyedTags(): SparseArray<*> {
        return viewKeyedTagsField?.get(this) as SparseArray<*>? ?: SparseArray<Nothing>(0)
    }

    private inline fun SparseArray<*>.first(predicate: (Any?) -> Boolean): Any? {
        for (i in 0 until size()) {
            val item = valueAt(i)
            if (predicate(item)) return item
        }
        return null
    }

    /**
     * If this is a `WrappedComposition`, returns the original composition, else returns this.
     */
    private fun Composition.unwrap(): Composition {
        if (this::class.java.name != WRAPPED_COMPOSITION_CLASS_NAME) return this
        return originalField.get(this) as Composition
    }


    /**
     * Tries to pull a [Composer] out of this [Composition], or returns null if it can't find one.
     */
    private fun Composition.getComposerOrNull(): Composer<*>? {
        if (this::class.java.name != COMPOSITION_IMPL_CLASS_NAME) return null
        return composerField.get(this) as? Composer<*>
    }

    /**
     * A sequence that lazily parses [ComposeLayoutInfo]s from a [Group] tree.
     */
    internal val Group.layoutInfos: Sequence<ComposeLayoutInfo> get() = computeLayoutInfos()

    /**
     * Recursively parses [ComposeLayoutInfo]s from a [Group]. Groups form a tree and can contain different
     * type of nodes which represent function calls, arbitrary data stored directly in the slot table,
     * or just subtrees.
     *
     * This function walks the tree and collects only Groups which represent emitted values
     * ([NodeGroup]s). These either represent `LayoutNode`s (Compose's internal primitive for layout
     * algorithms) or classic Android views that the composition emitted. This function collapses all
     * the groups in between each of these nodes, but uses the top-most Group under the previous node
     * to derive the "name" of the [ComposeLayoutInfo]. The other [ComposeLayoutInfo] properties come directly off
     * [NodeGroup] values.
     */
    private fun Group.computeLayoutInfos(
        parentName: String = ""
    ): Sequence<ComposeLayoutInfo> {
        val name = parentName.ifBlank { this.name }.orEmpty()

        // Look for any CompositionReferences stored in this group. These will be rolled up into the
        // SubcomposeLayout if present, otherwise they will just be shown as regular children.
        val subComposedChildren = getCompositionReferences()
            .flatMap { it.tryGetComposers().asSequence() }
            .map { subcomposer ->
                ComposeLayoutInfo(
                    isSubcomposition = true,
                    name = name,
                    bounds = box,
                    modifiers = emptyList(),
                    // The compositionData val is marked as internal, and not intended for public consumption.
                    children = subcomposer.compositionData.asTree().layoutInfos,
                    view = null
                )
            }

        // SubcomposeLayouts need to be handled specially, because all their subcompositions are always
        // logical children of their single LayoutNode. In order to render them so that the rendering
        // actually matches that logical structure, we need to reorganize the subtree a bit so
        // subcompositions are children of the layout node and not siblings of it.
        //
        // Note that there's no sure-fire way to actually detect a SubcomposeLayout. The best we can do is
        // use a heuristic. If any part of the heuristics don't match, then we fall back to treating the
        // group like any other.
        //
        // The heuristic we use is:
        //  - Name of the group is "SubcomposeLayout".
        //  - Has one or more subcompositions under it.
        //  - Has exactly one LayoutNode child.
        //  - That LayoutNode has no children of its own.
        if (this.name == "SubcomposeLayout") {
            val (subcompositions, regularChildren) =
                (children.asSequence()
                    .flatMap { it.computeLayoutInfos(name) } + subComposedChildren)
                    .partition { it.isSubcomposition }

            if (subcompositions.isNotEmpty() && regularChildren.size == 1) {
                val mainNode = regularChildren.single()
                if (mainNode.children.isEmpty()) {
                    // We can be pretty confident at this point that this is an actual SubcomposeLayout, so
                    // expose its layout node as the parent of all its subcompositions.
                    val subcompositionName = "<subcomposition of ${mainNode.name}>"
                    return sequenceOf(
                        mainNode.copy(children = subcompositions.asSequence()
                            .map { it.copy(name = subcompositionName) }
                        )
                    )
                }
            }
        }

        // This is an intermediate group that doesn't represent a LayoutNode.
        if (this !is NodeGroup) {
            return children.asSequence()
                .flatMap { it.computeLayoutInfos(name) } + subComposedChildren
        }

        val children = children.asSequence()
            // This node will "consume" the name, so reset it name to empty for children.
            .flatMap { it.computeLayoutInfos() }

        val layoutInfo = ComposeLayoutInfo(
            name = name,
            bounds = box,
            modifiers = modifierInfo.map { it.modifier },
            children = children + subComposedChildren,
            view = node as? View
        )
        return sequenceOf(layoutInfo)
    }

    internal fun Group.getCompositionReferences(): Sequence<CompositionReference> {
        return REFLECTION_CONSTANTS?.run {
            data.asSequence()
                .filter { it != null && it::class.java == compositionReferenceHolderClass }
                .mapNotNull { holder -> holder.tryGetCompositionReference() }
        } ?: emptySequence()
    }

    @Suppress("UNCHECKED_CAST")
    internal fun CompositionReference.tryGetComposers(): Iterable<Composer<*>> {
        return REFLECTION_CONSTANTS?.let {
            if (!it.compositionReferenceImplClass.isInstance(this)) return emptyList()
            it.compositionReferenceImplComposersField.get(this) as? Iterable<Composer<*>>
        } ?: emptyList()
    }

    private fun Any?.tryGetCompositionReference() = REFLECTION_CONSTANTS?.let {
        it.compositionReferenceHolderRefField.get(this) as? CompositionReference
    }


    private fun Sequence<*>.isEmpty(): Boolean = !iterator().hasNext()

    private const val COMPOSITION_IMPL_CLASS_NAME =
        "androidx.compose.runtime.CompositionImpl"
    private const val WRAPPED_COMPOSITION_CLASS_NAME =
        "androidx.compose.ui.platform.WrappedComposition"

    private val viewKeyedTagsField: Field? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        try {
            View::class.java.getDeclaredField("mKeyedTags")
                .apply { isAccessible = true }
        } catch (e: NoSuchFieldException) {
            // Some devices don't have this field apparently.
            // See https://github.com/square/radiography/issues/119.
            null
        }
    }

    private val REFLECTION_CONSTANTS by lazy(LazyThreadSafetyMode.PUBLICATION) {
        try {
            object {
                val compositionReferenceHolderClass =
                    Class.forName("androidx.compose.runtime.Composer\$CompositionReferenceHolder")
                val compositionReferenceImplClass =
                    Class.forName("androidx.compose.runtime.Composer\$CompositionReferenceImpl")
                val compositionReferenceHolderRefField =
                    compositionReferenceHolderClass.getDeclaredField("ref")
                        .apply { isAccessible = true }
                val compositionReferenceImplComposersField =
                    compositionReferenceImplClass.getDeclaredField("composers")
                        .apply { isAccessible = true }
            }
        } catch (e: Throwable) {
            null
        }
    }

    private val originalField: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
        Class.forName(WRAPPED_COMPOSITION_CLASS_NAME)
            .getDeclaredField("original")
            .apply { isAccessible = true }
    }

    private val composerField: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
        Class.forName(COMPOSITION_IMPL_CLASS_NAME)
            .getDeclaredField("composer")
            .apply { isAccessible = true }
    }
}

/**
 * True if this view looks like the private view type that Compose uses to host compositions.
 * It does a fuzzy match to try to detect unsupported Compose versions, which will not be rendered
 * but will at least warn that the version is unsupported.
 */
internal val View.mightBeComposeView: Boolean
    get() = "AndroidComposeView" in this::class.java.name

/**
 * Information about a Compose `LayoutNode`, extracted from a [Group] tree via [Group.layoutInfos].
 *
 * This is a useful layer of indirection from directly handling Groups because it allows us to
 * define our own notion of what an atomic unit of "composable" is independently from how Compose
 * actually represents things under the hood. When this changes in some future dev version, we
 * only need to update the "parsing" logic in this file.
 * It's also helpful since we actually gather data from multiple Groups for a single LayoutInfo,
 * so parsing them ahead of time into these objects means the visitor can be stateless.
 */
data class ComposeLayoutInfo(
    val name: String,
    val bounds: IntBounds,
    val modifiers: List<Modifier>,
    val children: Sequence<ComposeLayoutInfo>,
    val view: View?,
    val isSubcomposition: Boolean = false
) {

    override fun hashCode(): Int = Objects.hash(name, bounds, view, isSubcomposition)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ComposeLayoutInfo) return false
        if (name != other.name) return false
        if (bounds != other.bounds) return false
        if (view != other.view) return false
        if (isSubcomposition != other.isSubcomposition) return false
        return true
    }
}