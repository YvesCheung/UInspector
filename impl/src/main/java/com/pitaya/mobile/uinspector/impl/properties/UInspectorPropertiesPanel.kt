package com.pitaya.mobile.uinspector.impl.properties

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pitaya.mobile.uinspector.UInspector
import com.pitaya.mobile.uinspector.hierarchy.AndroidView
import com.pitaya.mobile.uinspector.hierarchy.Layer
import com.pitaya.mobile.uinspector.impl.R
import com.pitaya.mobile.uinspector.properties.ViewProperties
import com.pitaya.mobile.uinspector.state.UInspectorTargetViews
import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.pitaya.mobile.uinspector.util.dpStr
import kotlinx.android.synthetic.main.uinspector_panel_properties.view.*
import kotlinx.android.synthetic.main.uinspector_view_layout.view.*

/**
 * @author YvesCheung
 * 2020/12/31
 */
class UInspectorPropertiesPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Properties"

    private var adapter: ViewPropsAdapter? = null

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_properties, null)

        val targets =
            UInspector.currentState.withLifecycle?.lastTargetViews
        val targetLayer: Layer? = targets?.target
        val targetView = (targetLayer as? AndroidView)?.view
        if (targetView != null) {

            root.view_props_list.adapter = ViewPropsAdapter(targetView).also {
                adapter = it
                targets.addOnDrawListener(it)
            }

            root.uinspector_view_margin.let {
                it.setBackgroundColor(Color.parseColor("#D3FF93"))
                it.view_prop.text = "margin"

                val lp =
                    targetView.layoutParams as? ViewGroup.MarginLayoutParams
                it.view_top.text = lp?.topMargin?.dpStr ?: 0.dpStr
                it.view_bottom.text = lp?.bottomMargin?.dpStr ?: 0.dpStr
                it.view_left.text = lp?.leftMargin?.dpStr ?: 0.dpStr
                it.view_right.text = lp?.rightMargin?.dpStr ?: 0.dpStr
            }

            root.uinspector_view_padding.let {
                it.setBackgroundColor(Color.parseColor("#ACD6FF"))
                it.view_prop.text = "padding"

                it.view_top.text = targetView.paddingTop.dpStr
                it.view_bottom.text = targetView.paddingBottom.dpStr
                it.view_left.text = targetView.paddingLeft.dpStr
                it.view_right.text = targetView.paddingRight.dpStr
            }

            root.uinspector_view_bound.let {
                it.setBackgroundColor(Color.parseColor("#FFFFCE"))
                it.view_top.text =
                    targetView.width.dpStr + "\nX\n" + targetView.height.dpStr
            }
        }
        return root
    }

    override fun onDestroyView() {
        adapter?.let {
            UInspector.currentState.withLifecycle?.lastTargetViews?.removeOnDrawListener(it)
        }
    }

    private class ViewPropsAdapter(val targetView: View) :
        RecyclerView.Adapter<ViewPropsHolder>(), UInspectorTargetViews.Listener {

        private var props: List<Pair<String, Any?>> = ViewProperties(targetView).toList()

        override fun onChange() {
            val oldProps = props
            props = ViewProperties(targetView).toList()
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun getOldListSize(): Int = oldProps.size

                override fun getNewListSize(): Int = props.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    oldProps[oldItemPosition].first == props[newItemPosition].first

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    oldProps[oldItemPosition].second == props[newItemPosition].second

            }).dispatchUpdatesTo(this)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPropsHolder {
            val textView = TextView(parent.context)
            textView.textSize = 11f
            textView.setTextColor(Color.WHITE)
            textView.isSingleLine = false
            textView.movementMethod = LinkMovementMethod.getInstance()
            return ViewPropsHolder(textView)
        }

        override fun getItemCount(): Int = props.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewPropsHolder, position: Int) {
            val (name, value) = props[position]
            val s = SpannableStringBuilder(name)
                .append(": ")
                .append(if (value is CharSequence) value else value.toString())
            s.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.uinspector_primary_color
                    )
                ),
                0,
                name.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.text.text = s
        }
    }

    private class ViewPropsHolder(val text: TextView) : RecyclerView.ViewHolder(text)
}