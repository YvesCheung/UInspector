package com.pitaya.mobile.uinspector.hierarchy

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/9/30
 */
object FragmentsFinder {

    fun findFragments(activity: Activity): Map<View, Any> {
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