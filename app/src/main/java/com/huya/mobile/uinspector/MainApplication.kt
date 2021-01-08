package com.huya.mobile.uinspector

import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @author YvesCheung
 * 2021/1/7
 */
@Suppress("unused")
class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}