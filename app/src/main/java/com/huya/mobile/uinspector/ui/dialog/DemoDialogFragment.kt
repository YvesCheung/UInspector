package com.huya.mobile.uinspector.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.huya.mobile.uinspector.demo.R

/**
 * @author YvesCheung
 * 2020/12/30
 */
class DemoDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_demo, container, false)
    }
}