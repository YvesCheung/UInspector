-keep class com.pitaya.mobile.uinspector.plugins.UInspectorPluginService {*;}
-keep class * implements com.pitaya.mobile.uinspector.plugins.UInspectorPluginService {*;}

-assumenosideeffects class com.pitaya.mobile.uinspector.plugins.FastServiceLoaderKt {
    boolean ENABLE_FAST_SPI return false;
}