package com.pitaya.mobile.uinspector.optional.fresco

import android.util.Log
import android.view.View
import com.facebook.datasource.DataSource
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.drawee.view.DraweeView
import com.facebook.imagepipeline.request.HasImageRequest
import com.pitaya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
open class FrescoPropertiesParserPlugin : ViewPropertiesPlugin {

    override val uniqueKey: String = "Fresco"

    override fun tryCreate(view: View): ViewPropertiesParser<out View>? {
        if (!noSuchMethod && view is DraweeView<*>) {
            val c = view.controller
            if (c is AbstractDraweeController<*, *>) {
                try {
                    val dataSource = getDataSourceMethod.invoke(c) as? DataSource<*>
                    if (dataSource is HasImageRequest) {
                        val request = dataSource.imageRequest
                        if (request != null) {
                            return DraweeViewPropertiesParser(view, request)
                        }
                    }
                } catch (e: NoSuchMethodException) {
                    Log.e("UInspector", e.toString())
                    noSuchMethod = true
                } catch (e: Throwable) {
                    Log.e("UInspector", e.toString())
                }
            }
        }
        return null
    }

    companion object {

        private var noSuchMethod = false

        private val getDataSourceMethod by lazy(LazyThreadSafetyMode.NONE) {
            val m = AbstractDraweeController::class.java.getDeclaredMethod("getDataSource")
            m.isAccessible = true
            m
        }
    }
}