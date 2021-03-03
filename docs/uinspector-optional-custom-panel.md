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

2. Register your plugin to `UInspectorPluginService`

       ```kotlin
       package com.example
       import com.pitaya.mobile.uinspector.plugins.UInspectorPluginService
       import com.pitaya.mobile.uinspector.plugins.UInspectorPlugins
       import com.pitaya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelPlugin

       class YourPluginService : UInspectorPluginService {

           override fun onCreate(context: Context, plugins: UInspectorPlugins) {
               plugins.prepend(UInspectorChildPanelPlugin::class.java, YourPlugin())
           }
       }
       ```

3. Create a file named `com.pitaya.mobile.uinspector.plugins.UInspectorPluginService` in the directory `src/main/resources/META-INF/services`.
Write down your service class name `YourPluginService` in the file.

    ```
    com.example.YourPluginService
    ```
   
    See the sample in [src/main/resources/META-INF/services](https://github.com/YvesCheung/UInspector/blob/2.x/impl/src/main/resources/META-INF/services/com.pitaya.mobile.uinspector.plugins.UInspectorPluginService)

4. OK! Now run your app!