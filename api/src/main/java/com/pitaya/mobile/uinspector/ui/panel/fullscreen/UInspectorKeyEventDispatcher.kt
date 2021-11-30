package com.pitaya.mobile.uinspector.ui.panel.fullscreen

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import androidx.annotation.StyleRes
import com.github.yvescheung.whisper.Input

/**
 * @author YvesCheung
 * 2021/1/3
 */
class UInspectorKeyEventDispatcher(context: Context, @StyleRes theme: Int) : Dialog(context, theme) {

    @Input
    var onKeyEvent: ((KeyEvent) -> Boolean)? = null

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return onKeyEvent?.invoke(event) ?: super.dispatchKeyEvent(event)
    }
}