package com.pitaya.mobile.uinspector.impl.utils

import android.content.res.Resources

/**
 * @author YvesCheung
 * 2021/1/16
 */
internal val Int.dpToPx: Int
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

internal val Number.dpToPx: Float
    get() = this.toFloat() * Resources.getSystem().displayMetrics.density