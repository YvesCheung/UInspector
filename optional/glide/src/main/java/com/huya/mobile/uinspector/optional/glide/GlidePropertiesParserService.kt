package com.huya.mobile.uinspector.optional.glide

import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.R
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.ViewTarget
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService

/**
 * @author YvesCheung
 * 2021/1/7
 */
class GlidePropertiesParserService : ViewPropertiesParserService {

    override fun tryCreate(v: View): ViewPropertiesParser<out View>? {
        var tag = v.getTag(customViewTargetId)
        if (tag == null) {
            val oldId = getViewTargetId()
            if (oldId > 0) {
                tag = v.getTag(oldId)
            }
        }
        if (tag == null) {
            tag = v.tag
        }
        if (tag is Request) {
            return if (v is ImageView) {
                GlideImageViewParser(v, tag)
            } else {
                GlideViewParser(v, tag)
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