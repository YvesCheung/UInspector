package com.pitaya.mobile.uinspector.optional.compose.inspect

import android.util.SparseArray
import android.view.View
import androidx.compose.runtime.Composer
import androidx.compose.runtime.Composition
import com.pitaya.mobile.uinspector.hierarchy.ErrorLayer
import com.pitaya.mobile.uinspector.hierarchy.Layer
import java.lang.reflect.Field
import kotlin.LazyThreadSafetyMode.PUBLICATION


private const val ANDROID_COMPOSE_VIEW_CLASS_NAME =
    "androidx.compose.ui.platform.AndroidComposeView"



/** Reflectively tries to determine if Compose is on the classpath. */
internal val isComposeAvailable by lazy(PUBLICATION) {
    try {
        Class.forName(ANDROID_COMPOSE_VIEW_CLASS_NAME)
        true
    } catch (e: Throwable) {
        false
    }
}





