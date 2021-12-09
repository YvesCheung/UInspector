package com.pitaya.mobile.uinspector.optional.compose.properties

import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Density
import com.github.yvescheung.whisper.Output

/**
 * @author YvesCheung
 * 2021/12/3
 */
abstract class GraphicsLayerModifier : ComposePropertiesParser {

    override val priority: Int = 9000

    override fun parse(@Output props: MutableMap<String, Any?>) {
        val config = parseConfig()
        if (config != null) {
            if (config != Default) {
                props["Modifier.graphicsLayer"] = ""
            }
            if (config.alpha != Default.alpha) {
                props["alpha"] = config.alpha
            }
            if (config.cameraDistance != Default.cameraDistance) {
                props["cameraDistance"] = config.cameraDistance
            }
            if (config.clip != Default.clip) {
                props["clip"] = config.clip
            }
            if (config.rotationX != Default.rotationX) {
                props["rotationX"] = config.rotationX
            }
            if (config.rotationY != Default.rotationY) {
                props["rotationY"] = config.rotationY
            }
            if (config.rotationZ != Default.rotationZ) {
                props["rotationZ"] = config.rotationZ
            }
            if (config.scaleX != Default.scaleX) {
                props["scaleX"] = config.scaleX
            }
            if (config.scaleY != Default.scaleY) {
                props["scaleY"] = config.scaleY
            }
            if (config.translationX != Default.translationX) {
                props["translationX"] = config.translationX
            }
            if (config.translationY != Default.translationY) {
                props["translationY"] = config.translationY
            }
            if (config.shadowElevation != Default.shadowElevation) {
                props["shadowElevation"] = config.shadowElevation
            }
            if (config.shape != Default.shape) {
                props["shape"] = config.shape
            }
            if (config.transformOrigin != Default.transformOrigin) {
                props["transformOrigin"] = config.transformOrigin
            }
        }
    }

    protected abstract fun parseConfig(): GraphicsConfig?

    data class GraphicsConfig(
        override var alpha: Float = 1f,
        override var cameraDistance: Float = DefaultCameraDistance,
        override var clip: Boolean = false,
        override var rotationX: Float = 0f,
        override var rotationY: Float = 0f,
        override var rotationZ: Float = 0f,
        override var scaleX: Float = 1f,
        override var scaleY: Float = 1f,
        override var shadowElevation: Float = 0f,
        override var shape: Shape = RectangleShape,
        override var transformOrigin: TransformOrigin = TransformOrigin.Center,
        override var translationX: Float = 0f,
        override var translationY: Float = 0f,
    ) : GraphicsLayerScope {

        private var graphicsDensity: Density = Density(1.0f)

        override val density: Float
            get() = graphicsDensity.density

        override val fontScale: Float
            get() = graphicsDensity.fontScale
    }

    companion object {

        protected val Default = GraphicsConfig()
    }
}