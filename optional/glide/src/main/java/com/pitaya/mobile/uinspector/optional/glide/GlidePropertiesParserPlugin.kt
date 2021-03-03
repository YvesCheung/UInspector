package com.pitaya.mobile.uinspector.optional.glide

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.R
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.ViewTarget
import com.pitaya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.pitaya.mobile.uinspector.properties.view.ViewPropertiesPlugin

/**
 * @author YvesCheung
 * 2021/2/1
 */
open class GlidePropertiesParserPlugin : ViewPropertiesPlugin {

    override val uniqueKey: String = "Glide"

    override fun tryCreate(view: View): ViewPropertiesParser<out View>? {
        var tag = view.getTag(customViewTargetId)
        if (tag == null) {
            val oldId = getViewTargetId()
            if (oldId > 0) {
                tag = view.getTag(oldId)
            }
        }
        if (tag == null) {
            tag = view.tag
        }
        if (tag is Request) {
            return if (view is ImageView) {
                GlideImageViewParser(view, tag)
            } else {
                GlideViewParser(view, tag)
            }
        }
        return null
    }

    companion object {

        /**
         * @see CustomViewTarget.VIEW_TAG_ID
         */
        private val customViewTargetId = R.id.glide_custom_view_target_tag

        private var noTagIdField = false

        /**
         * @see ViewTarget.tagId
         */
        private fun getViewTargetId(): Int {
            if (!noTagIdField) {
                try {
                    return viewTargetTagIdField.get(null) as Int
                } catch (e: NoSuchFieldException) {
                    noTagIdField = true
                    Log.e("UInspector", e.toString())
                } catch (e: Throwable) {
                    Log.e("UInspector", e.toString())
                }
            }
            return -1
        }

        private val viewTargetTagIdField by lazy(LazyThreadSafetyMode.NONE) {
            val f = ViewTarget::class.java.getDeclaredField("tagId")
            f.isAccessible = true
            f
        }
    }
}