package com.huya.mobile.uinspector.impl.hierarchy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.huya.mobile.uinspector.UInspector
import com.huya.mobile.uinspector.impl.R
import com.huya.mobile.uinspector.impl.utils.idToString
import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
import com.yy.mobile.whisper.Output
import kotlinx.android.synthetic.main.uinspector_panel_hierarchy.view.*

/**
 * @author YvesCheung
 * 2021/1/2
 */
class UInspectorHierarchyPanel(override val priority: Int) : UInspectorChildPanel {

    override val title = "Hierarchy"

    @SuppressLint("InflateParams")
    override fun onCreateView(context: Context): View {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.uinspector_panel_hierarchy, null)
        val lifecycleState = UInspector.currentState.withLifecycle
        val hierarchy = lifecycleState?.lastTouchTargets
        val activity = lifecycleState?.activity
        if (hierarchy != null && activity != null) {
            val records = findFragments(activity)

            val ssb = SpannableStringBuilder()

            ssb.withColor {
                ssb.newLine(0) {
                    append(activity::class.java.canonicalName)
                }
            }

            for ((index, view) in hierarchy.withIndex()) {
                val fragment = records[view]
                if (fragment != null) {
                    ssb.withColor {
                        newLine(index) {
                            append(fragment::class.java.canonicalName)
                        }
                    }
                }

                if (index == hierarchy.lastIndex) {
                    ssb.withColor {
                        ssb.newLine(index) {
                            append(view::class.java.simpleName)
                            if (view.id > 0) {
                                append("(${idToString(view.context, view.id)})")
                            }
                        }
                    }
                } else {
                    ssb.newLine(index) {
                        append(view::class.java.simpleName)
                        if (view.id > 0) {
                            append("(${idToString(view.context, view.id)})")
                        }
                    }
                }
            }
            root.view_hierarchy.text = ssb
        }
        return root
    }

    private fun findFragments(activity: Activity): Map<View, Any> {
        val records = mutableMapOf<View, Any>()
        if (activity is FragmentActivity) {
            record(activity.supportFragmentManager, records)
        }
        record(activity.fragmentManager, records)
        return records
    }

    private fun record(fm: FragmentManager, @Output records: MutableMap<View, Any>) {
        for (fragment in fm.fragments) {
            val v = fragment.view
            if (v != null) {
                records[v] = fragment
            }
            record(fragment.childFragmentManager, records)
        }
    }

    private fun record(fm: android.app.FragmentManager, @Output records: MutableMap<View, Any>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (fragment in fm.fragments) {
                val v = fragment.view
                if (v != null) {
                    records[v] = fragment
                }
                record(fragment.childFragmentManager, records)
            }
        }
    }

    private inline fun SpannableStringBuilder.newLine(
        indentSize: Int,
        write: SpannableStringBuilder.() -> Unit
    ) {
        var size = indentSize
        while (size-- > 0) {
            append(" ")
        }
        append("- ")
        write()
        append("\n")
    }

    private inline fun SpannableStringBuilder.withColor(write: SpannableStringBuilder.() -> Unit) {
        val start = length
        write()
        val end = length
        setSpan(
            ForegroundColorSpan(Color.parseColor("#66B3FF")),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}