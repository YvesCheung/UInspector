package com.huya.mobile.uinspector.hierarchy

import android.view.View

/**
 * @author YvesCheung
 * 2021/1/29
 */
interface LayerFactoryService {

    fun create(view: View): Layer?
}