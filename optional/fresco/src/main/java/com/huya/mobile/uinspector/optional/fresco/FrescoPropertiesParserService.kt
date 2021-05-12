package com.huya.mobile.uinspector.optional.fresco

import android.util.Log
import android.view.View
import com.facebook.datasource.DataSource
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.drawee.view.DraweeView
import com.facebook.imagepipeline.request.HasImageRequest
import com.google.auto.service.AutoService
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService

/**
 * @author YvesCheung
 * 2021/1/7
 */
@AutoService(ViewPropertiesParserService::class)
class FrescoPropertiesParserService : ViewPropertiesParserService {

    override fun tryCreate(v: View): ViewPropertiesParser<out View>? {
        if (!noSuchMethod && v is DraweeView<*>) {
            val c = v.controller
            if (c is AbstractDraweeController<*, *>) {
                try {
                    val dataSource = getDataSourceMethod.invoke(c) as? DataSource<*>
                    if (dataSource is HasImageRequest) {
                        val request = dataSource.imageRequest
                        if (request != null) {
                            return DraweeViewPropertiesParser(v, request)
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