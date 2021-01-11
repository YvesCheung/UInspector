package com.huya.mobile.uinspector.impl.properties

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
import androidx.recyclerview.widget.RecyclerView
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.huya.mobile.uinspector.util.dpStr
import kotlinx.android.synthetic.main.uinspector_panel_properties.view.*
import kotlinx.android.synthetic.main.uinspector_view_layout.view.*

/**
 * @author YvesCheung
 * 2020/12/31
 */
class UInspectorPropertiesPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Properties"

    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_properties, null)

        val targetView =
            UInspector.currentState.withLifecycle?.lastTargetViews?.lastOrNull()
        if (targetView != null) {

            root.view_props_list.adapter = ViewPropsAdapter(ViewProperties(targetView))

            root.uinspector_view_margin.let {
                it.setBackgroundColor(Color.parseColor("#D3FF93"))
                it.view_prop.text = "margin"

                val lp =
                    targetView.layoutParams as? ViewGroup.MarginLayoutParams
                it.view_top.text = lp?.topMargin?.dpStr ?: "0dp"
                it.view_bottom.text = lp?.bottomMargin?.dpStr ?: "0dp"
                it.view_left.text = lp?.leftMargin?.dpStr ?: "0dp"
                it.view_right.text = lp?.rightMargin?.dpStr ?: "0dp"
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
                    targetView.measuredWidth.dpStr + "\nX\n" + targetView.measuredHeight.dpStr
            }
        }
        return root
    }

    private class ViewPropsAdapter(properties: ViewProperties) :
        RecyclerView.Adapter<ViewPropsHolder>() {

        private val displayProp = properties.toList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPropsHolder {
            val textView = TextView(parent.context)
            textView.textSize = 11f
            textView.setTextColor(Color.WHITE)
            textView.isSingleLine = false
            return ViewPropsHolder(textView)
        }

        override fun getItemCount(): Int = displayProp.size

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewPropsHolder, position: Int) {
            val (name, value) = displayProp[position]
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
            holder.text.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private class ViewPropsHolder(val text: TextView) : RecyclerView.ViewHolder(text)
}