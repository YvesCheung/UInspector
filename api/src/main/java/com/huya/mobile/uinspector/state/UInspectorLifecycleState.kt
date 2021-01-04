package com.huya.mobile.uinspector.state

import android.app.Activity
import android.view.View
import com.huya.mobile.uinspector.ui.panel.fullscreen.UInspectorPanel
import com.yy.mobile.whisper.NotThreadSafe

/**
 * Store the information which is bound to the lifecycle of [activity]
 *
 * @author YvesCheung
 * 2020/12/30
 */
@NotThreadSafe
class UInspectorLifecycleState(val activity: Activity) {

    var panel: UInspectorPanel? = null
        internal set

    var lastTargetViews: List<View>? = null
        internal set
}