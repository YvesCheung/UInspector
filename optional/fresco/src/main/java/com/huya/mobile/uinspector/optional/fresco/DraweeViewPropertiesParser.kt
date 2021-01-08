package com.huya.mobile.uinspector.optional.fresco

import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.view.DraweeView
import com.facebook.imagepipeline.common.SourceUriType
import com.facebook.imagepipeline.request.ImageRequest
import com.huya.mobile.uinspector.impl.properties.view.ImageViewPropertiesParser
import com.yy.mobile.whisper.Output

/**
 * @author YvesCheung
 * 2021/1/7
 */
class DraweeViewPropertiesParser(private val draweeView: DraweeView<*>, private val request: ImageRequest) :
    ImageViewPropertiesParser(draweeView) {

    override fun parse(@Output props: MutableMap<String, Any?>) {
        super.parse(props)

        val hierarchy = draweeView.hierarchy
        if (hierarchy is GenericDraweeHierarchy) {
            props["scaleType"] = "fresco ${hierarchy.actualImageScaleType}"
        }

        val uri = request.sourceUri
        props["fresco src"] = if (uri != null) clickable(uri) else "null"

        val type = request.sourceUriType
        props["fresco src type"] =
            when (type) {
                SourceUriType.SOURCE_TYPE_NETWORK -> "NETWORK"
                SourceUriType.SOURCE_TYPE_LOCAL_FILE -> "FILE"
                SourceUriType.SOURCE_TYPE_LOCAL_VIDEO_FILE -> "VIDEO_FILE"
                SourceUriType.SOURCE_TYPE_LOCAL_IMAGE_FILE -> "IMAGE_FILE"
                SourceUriType.SOURCE_TYPE_LOCAL_CONTENT -> "CONTENT"
                SourceUriType.SOURCE_TYPE_LOCAL_ASSET -> "ASSET"
                SourceUriType.SOURCE_TYPE_LOCAL_RESOURCE -> "RESOURCE"
                SourceUriType.SOURCE_TYPE_DATA -> "DATA"
                SourceUriType.SOURCE_TYPE_QUALIFIED_RESOURCE -> "QUALIFIED_RESOURCE"
                else -> "UNKNOWN"
            }

        val config = request.imageDecodeOptions?.bitmapConfig
        if (config != null) {
            props["fresco decode"] = config
        }

        props["fresco prefer width"] = request.preferredWidth
        props["fresco prefer height"] = request.preferredHeight


        val rotationOption = request.rotationOptions
        if (rotationOption != null) {
            when {
                rotationOption.useImageMetadata() ->
                    props["fresco rotation"] = "AUTO"
                !rotationOption.rotationEnabled() ->
                    props["fresco rotation"] = "DISABLE"
                rotationOption.forcedAngle > 0 ->
                    props["fresco rotation"] = rotationOption.forcedAngle
            }
        }
    }

    private fun clickable(uri: Uri): CharSequence {
        val str = uri.toString()
        if (!uri.scheme.isNullOrBlank()) {
            val s = SpannableString(str)
            s.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    view.context.startActivity(intent)
                }
            }, 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return s
        }
        return str
    }
}