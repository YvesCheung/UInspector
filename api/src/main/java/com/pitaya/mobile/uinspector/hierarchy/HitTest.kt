package com.pitaya.mobile.uinspector.hierarchy

import android.view.MotionEvent

/**
 * Traverse the UI hierarchy to find the hit [Layer] of the more inner level
 *
 * @author YvesCheung
 * 2021/1/29
 */
interface HitTest {

    fun findNextTarget(event: MotionEvent, current: Layer): Layer?
}