package com.pitaya.mobile.uinspector.state

import androidx.annotation.MainThread
import com.pitaya.mobile.uinspector.UInspector

/**
 *
 * @see UInspector.currentState
 *
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

    /**
     * state bound to activity lifecycle
     */
    var withLifecycle: UInspectorLifecycleState? = null
}