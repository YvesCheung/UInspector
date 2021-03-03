package com.pitaya.mobile.uinspector.state

import android.app.Activity
import com.pitaya.mobile.uinspector.ui.panel.fullscreen.UInspectorPanel
import com.yy.mobile.whisper.NotThreadSafe
import com.yy.mobile.whisper.UseWith

/**
 * Store the information which is bound to the lifecycle of [activity]
 *
 * @author YvesCheung
 * 2020/12/30
 */
@NotThreadSafe
class UInspectorLifecycleState @UseWith("clear") constructor(val activity: Activity) {

    var panel: UInspectorPanel? = null
        internal set

    var lastTargetViews: UInspectorTargetViews? = null
        internal set

    internal fun clear() {
        panel?.close()
        panel = null
        lastTargetViews?.clear()
        lastTargetViews = null
    }
}