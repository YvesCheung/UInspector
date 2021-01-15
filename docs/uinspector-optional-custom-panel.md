# Develop your custom panel

**Using Java SPI mechanism, You can develop your own panel and add it into `Uinspector`**

1. Create your custom `UInspectorChildPanelService` and `UInspectorChildPanel` in your module:

    ```kotlin
    package com.example
    import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService
    import com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanel

    class YourPanelService: UInspectorChildPanelService {

        override fun createPanels(): Set<UInspectorChildPanel> {
            return setOf(YourPanel())
        }

        class YourPanel : UInspectorChildPanel{

            override val title = "YourPanelName"

            override fun onCreateView(context: Context): View {
                return ...
            }
        }
    }
    ```

2. Create a file named `com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService` in directory `src/main/resources/META-INF/services/`. Write down your class name in the file:

    ```
    com.example.YourPanelService
    ```

3. Ok! Now you can run your app and find `YourPanel` in the uinspector!

See the sample in [src/main/resources/META-INF/services](https://github.com/YvesCheung/UInspector/blob/master/impl/src/main/resources/META-INF/services/com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService)
