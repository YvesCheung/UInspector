package com.pitaya.mobile.uinspector.impl.hierarchy.extra

import android.app.Activity
import android.os.Build
import android.text.SpannableStringBuilder
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pitaya.mobile.uinspector.hierarchy.HierarchyExtraInfo
import com.pitaya.mobile.uinspector.util.newLine
import com.pitaya.mobile.uinspector.util.withColor
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/6
 */
@Suppress("MemberVisibilityCanBePrivate")
open class HierarchyFragmentInfo(val activity: Activity) : HierarchyExtraInfo {

    protected val records = findFragments(activity)

    override fun beforeHierarchy(index: Int, view: View, s: SpannableStringBuilder) {
        val fragment = records[view]
        if (fragment != null) {
            s.withColor(activity) {
                newLine(index) {
                    append(fragment::class.java.canonicalName ?: fragment::class.java.name)
                }
            }
        }
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
            } else if (fragment is DialogFragment) {
                val dv = fragment.dialog?.window?.decorView
                if (dv != null) {
                    records[dv] = fragment
                }
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
                } else if (fragment is android.app.DialogFragment) {
                    val dv = fragment.dialog?.window?.decorView
                    if (dv != null) {
                        records[dv] = fragment
                    }
                }
                record(fragment.childFragmentManager, records)
            }
        }
    }
}