package com.huya.mobile.uinspector.impl.targets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.OVAL
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.impl.utils.dpToPx
import com.huya.mobile.uinspector.ui.decoration.UInspectorDecoration
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.util.idToString
import com.huya.mobile.uinspector.util.visibilityToString
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
            val parent = targetView.parent
            val parentList: List<ViewInfo> =
                if (parent is View) listOf(ViewInfo(parent))
                else emptyList()
            setupList(root.view_targets_parent_title, root.view_targets_parent, parentList)

            val children: List<ViewInfo> =
                if (targetView is ViewGroup)
                    targetView.children.mapIndexedTo(mutableListOf()) { index, view ->
                        ViewInfo(view, index)
                    }
                else emptyList()
            setupList(root.view_targets_children_title, root.view_targets_children, children)

            val brothers: List<ViewInfo> =
                if (parent is ViewGroup)
                    parent.children.mapIndexedNotNullTo(mutableListOf()) { index, view ->
                        if (view !== targetView) ViewInfo(view, index)
                        else null
                    }
                else emptyList()
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
            val name = StringBuilder(target::class.java.simpleName)
            if (target.id > 0) {
                name.append("(${idToString(ctx, target.id)})")
            }
            if (index >= 0) {
                name.append("(index $index)")
            }
            if (target.visibility != VISIBLE) {
                holder.textView.setTextColor(ctx.resources.getColor(android.R.color.darker_gray))
                name.append("(${visibilityToString(target.visibility)})")
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
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || target.isAttachedToWindow) {
                    UInspector.currentState.withLifecycle?.panel?.updateTargetView(target)
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "View has been detached!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private class VH(val textView: TextView) : RecyclerView.ViewHolder(textView)

    private data class ViewInfo(
        val view: View,
        val index: Int = -1,
        @ColorInt val color: Int = ColorGenerator.next()
    )
}