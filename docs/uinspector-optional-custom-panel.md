# Develop your custom panel

**Using Java SPI mechanism, You can develop your own panel and add it into `Uinspector`**

1. Create your custom `UInspectorChildPanelPlugin` and `UInspectorChildPanel` in your module:

    ```kotlin
    import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel
    import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

    class YourPlugin: UInspectorChildPanelPlugin {

        override fun createPanels(): Set<UInspectorChildPanel> {
            return setOf(YourPanel())
        }

        class YourPanel : UInspectorChildPanel {

            override val title = "YourPanelName"

            override fun onCreateView(context: Context): View {
                return ...
            }
        }
    }
    ```

2. Register your plugin to `UInspectorPluginService`, which is annotated with `@AutoService`

    ```kotlin
    package com.example
    import com.google.auto.service.AutoService
    import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
    import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins
    import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

    @AutoService(UInspectorPluginService::class)
    class YourPluginService : UInspectorPluginService {

        override fun onCreate(context: Context, plugins: UInspectorPlugins) {
            plugins.prepend(UInspectorChildPanelPlugin::class.java, YourPlugin())
        }
    }
    ```

3. OK! Now run your app!