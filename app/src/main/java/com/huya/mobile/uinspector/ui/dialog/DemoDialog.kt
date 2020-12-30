package com.huya.mobile.uinspector.ui.dialog

import android.app.Dialog
import android.content.Context
import com.huya.mobile.uinspector.demo.R

/**
 * @author YvesCheung
 * 2020/12/30
 */
class DemoDialog(context: Context) : Dialog(context) {

    override fun show() {
        setContentView(R.layout.dialog_demo)
        super.show()
    }
}