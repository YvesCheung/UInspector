package com.pitaya.mobile.uinspector.impl.targets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.OVAL
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.impl.R
import com.pitaya.mobile.uinspector.impl.utils.dpToPx
import com.pitaya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.pitaya.mobile.uinspector.util.visibilityToString
import kotlinx.android.synthetic.main.uinspector_panel_targets.view.*
import kotlinx.android.synthetic.main.uinspector_view_list_item.view.*

/**
 * @author YvesCheung
 * 2021/1/4
 */
class UInspectorTargetsPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Targets"

    private val decorations = mutableListOf<UInspectorDecoration>()

    @SuppressLint("InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_targets, null)
        val targetView =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        if (targetView != null) {
            val parent: Layer? = targetView.parent
            val parentList: List<ViewInfo> =
                if (parent != null) listOf(ViewInfo(parent))
                else emptyList()
            setupList(root.view_targets_parent_title, root.view_targets_parent, parentList)

            val children: List<ViewInfo> =
                targetView.children.mapIndexedTo(mutableListOf()) { index, view ->
                    ViewInfo(view, index)
                }
            setupList(root.view_targets_children_title, root.view_targets_children, children)

            val brothers: List<ViewInfo> =
                parent?.children?.mapIndexedNotNullTo(mutableListOf()) { index, view ->
                    if (view != targetView) ViewInfo(view, index)
                    else null
                } ?: emptyList()
            setupList(root.view_targets_brother_title, root.view_targets_brother, brothers)
        }
        return root
    }

    private fun setupList(title: View, list: RecyclerView, data: List<ViewInfo>) {
        if (data.isNotEmpty()) {
            data.forEach { (child, _, color) ->
                decorations.add(TargetStrokeDecoration(child, color))
            }
            title.visibility = VISIBLE
            list.visibility = VISIBLE
            list.adapter = Adapter(data)
        } else {
            title.visibility = GONE
            list.visibility = GONE
        }
    }

    override fun onUserVisibleHint(visible: Boolean) {
        if (visible) {
            decorations.forEach { decoration ->
                UInspector.currentState.withLifecycle?.panel?.addDecoration(decoration)
            }
        } else {
            decorations.forEach { decoration ->
                UInspector.currentState.withLifecycle?.panel?.removeDecoration(decoration)
            }
        }
        if (decorations.isNotEmpty()) {
            UInspector.currentState.withLifecycle?.panel?.invalidate()
        }
    }

    override fun onDestroyView() {
        decorations.forEach { decoration ->
            UInspector.currentState.withLifecycle?.panel?.removeDecoration(decoration)
        }
        if (decorations.isNotEmpty()) {
            UInspector.currentState.withLifecycle?.panel?.invalidate()
        }
        super.onDestroyView()
    }

    private inner class Adapter(val views: List<ViewInfo>) : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.uinspector_view_list_item, parent, false)
            return VH(root.uinspector_view_access_btn)
        }

        override fun getItemCount(): Int = views.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val ctx = holder.itemView.context
            val (target, index, color) = views[position]
            val name = StringBuilder(target.name)
            if (!target.id.isNullOrBlank()) {
                name.append("(").append(target.id).append(")")
            }
            if (index >= 0) {
                name.append("(index $index)")
            }
            if (target is AndroidView && target.view.visibility != VISIBLE) {
                holder.textView.setTextColor(ctx.resources.getColor(android.R.color.darker_gray))
                name.append("(${visibilityToString(target.view.visibility)})")
            }
            val colorMark = GradientDrawable().apply {
                shape = OVAL
                setColor(color)
                setBounds(0, 0, 20.dpToPx, 20.dpToPx)
            }
            holder.textView.setCompoundDrawables(colorMark, null, null, null)
            holder.textView.compoundDrawablePadding = 8.dpToPx
            holder.textView.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            holder.textView.text = name
            holder.textView.setOnClickListener {
                UInspector.currentState.withLifecycle?.panel?.updateTargetLayer(target)
            }
        }
    }

    private class VH(val textView: TextView) : RecyclerView.ViewHolder(textView)

    private data class ViewInfo(
        val layer: Layer,
        val index: Int = -1,
        @ColorInt val color: Int = ColorGenerator.next(layer)
    )
}