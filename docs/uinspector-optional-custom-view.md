# Inspect custom view properties

Using Java SPI mechanism, You can intergrate your custom `view`/`LayoutParams` properties into Uinspector. Such as [Uinspecor-Fresco](https://github.com/YvesCheung/UInspector/blob/master/docs/uinspector-optional-fresco.md) / [Uinspector-Lottie](https://github.com/YvesCheung/UInspector/blob/master/docs/uinspector-optional-lottie.md).

1. Create a class implements the `ViewPropertiesParserService`

    ```kotlin
    package com.your.pack
    
    import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
    import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService

    class YourService : ViewPropertiesParserService {

        override fun tryCreate(v: View): ViewPropertiesParser<out View>? {
            if (v is YourCustomView) {
                return YourCustomViewParser(v)
            }
            //Just return null if you don't want to handle this view
            return null
        }
    }
    ```
    
2. Create a class extends the `ViewPropertiesParser`

    ```kotlin
    import android.view.View
    import com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParser
    import com.yy.mobile.whisper.Output

    class YourCustomViewParser(view: YourCustomView) : ViewPropertiesParser<YourCustomView>(view) {

        override fun parse(@Output props: MutableMap<String, Any?>) {
            super.parse(props)
            props["key"] = value
        }
    }
    ```
    
3. Create a file named `com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService` in the directory `src/main/resources/META-INF/services`. Write down your service class name `YourService` in the file.

    [See the demo: /src/main/resources/META-INF/services/](https://github.com/YvesCheung/UInspector/blob/master/optional/glide/src/main/resources/META-INF/services/com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService)
    
4. OK! Now run your app and click the `YourCustomView` on screen. 