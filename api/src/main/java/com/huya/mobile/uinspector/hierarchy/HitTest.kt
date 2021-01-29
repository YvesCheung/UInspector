package com.huya.mobile.uinspector.hierarchy

import android.view.MotionEvent

/**
 * @author YvesCheung
 * 2021/1/29
 */
interface HitTest {

    fun findNextTarget(event: MotionEvent, current: Layer): Layer?
}