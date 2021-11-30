package com.pitaya.mobile.uinspector.optional.glide

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import com.bumptech.glide.request.*
import com.pitaya.mobile.uinspector.util.drawableToString
import com.pitaya.mobile.uinspector.util.idToString
import com.pitaya.mobile.uinspector.util.link
import com.github.yvescheung.whisper.Output
import java.io.File
import java.net.URI
import java.net.URL


/**
 * @author YvesCheung
 * 2021/1/7
 */
class GlideRequestParser(
    private val context: Context,
    private val request: Request
) {

    fun parse(@Output props: MutableMap<String, Any?>) {
        try {
            var req: Request? = request
            while (req != null) {
                if (req is SingleRequest<*>) {
                    break
                } else if (req is ThumbnailRequestCoordinator) {
                    req = fullRequest.get(request) as? Request
                } else if (req is ErrorRequestCoordinator) {
                    req = primaryRequest.get(request) as? Request
                }
            }

            if (req is SingleRequest<*>) {
                val model = modelField.get(req)
                props["glide model"] =
                    when (model) {
                        is Int -> idToString(context, model)
                        is File -> "File(\"${model.absolutePath}\")"
                        is Uri -> clickable(model.scheme, model.toString())
                        is String -> clickable(Uri.parse(model).scheme, model)
                        is URI -> clickable(model.scheme, model.toString())
                        is URL -> clickable(model.protocol, model.toString())
                        is ByteArray -> "byte[${model.size}]"
                        is Drawable -> drawableToString(model)
                        else -> model
                    }

                val requestOption =
                    requestOptionsField.get(req) as? BaseRequestOptions<*>
                if (requestOption != null) {
                    val ed = requestOption.getErrorPlaceholder()
                    if (ed != null) {
                        props["glide error"] = drawableToString(ed)
                    } else if (requestOption.getErrorId() > 0) {
                        props["glide error"] =
                            idToString(context, requestOption.getErrorId())
                    }

                    val pd = requestOption.getPlaceholderDrawable()
                    if (pd != null) {
                        props["glide placeholder"] = drawableToString(pd)
                    } else if (requestOption.getPlaceholderId() > 0) {
                        props["glide placeholder"] =
                            idToString(context, requestOption.getPlaceholderId())
                    }

                    val fd = requestOption.getFallbackDrawable()
                    if (fd != null) {
                        props["glide fallback"] = drawableToString(fd)
                    } else if (requestOption.getFallbackId() > 0) {
                        props["glide fallback"] =
                            idToString(context, requestOption.getFallbackId())
                    }
                }
            }
        } catch (e: Throwable) {
            Log.e("UInspector", e.toString())
        }
    }

    private fun clickable(scheme: String?, uriStr: String): CharSequence {
        if (scheme in listOf("http", "https")) {
            return link(uriStr) {
                try {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(uriStr))
                            .setClassName(
                                "com.android.browser",
                                "com.android.browser.BrowserActivity"
                            )
                    )
                } catch (e: Throwable) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriStr)))
                }
            }
        }
        return uriStr
    }

    companion object {

        private val fullRequest by lazy(LazyThreadSafetyMode.NONE) {
            val f = ThumbnailRequestCoordinator::class.java.getDeclaredField("full")
            f.isAccessible = true
            f
        }

        private val primaryRequest by lazy(LazyThreadSafetyMode.NONE) {
            val f = ErrorRequestCoordinator::class.java.getDeclaredField("primary")
            f.isAccessible = true
            f
        }

        private val modelField by lazy(LazyThreadSafetyMode.NONE) {
            val f = SingleRequest::class.java.getDeclaredField("model")
            f.isAccessible = true
            f
        }

        private val requestOptionsField by lazy(LazyThreadSafetyMode.NONE) {
            val f = SingleRequest::class.java.getDeclaredField("requestOptions")
            f.isAccessible = true
            f
        }
    }
}