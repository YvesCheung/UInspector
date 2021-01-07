package com.huya.mobile.uinspector

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @author YvesCheung
 * 2021/1/7
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}