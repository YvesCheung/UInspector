package com.huya.mobile.uinspector.state

import androidx.annotation.MainThread
import com.huya.mobile.uinspector.mask.UInspectorMask
import com.yy.mobile.whisper.NotThreadSafe

/**
 * @author YvesCheung
 * 2020/12/29
 */
class UInspectorState {

    /**
     * Indicate whether the inspector is running.
     *
     * If true, the user's touch event would be consumed by UInspector,
     * and hierarchical information would be shown at the anchor of the view.
     */
    @Volatile
    var isRunning: Boolean = false
        @MainThread internal set

    @NotThreadSafe
    internal var view: UInspectorMask? = null
}