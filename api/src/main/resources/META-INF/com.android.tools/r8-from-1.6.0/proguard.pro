# Allow R8 to optimize away the FastServiceLoader.
# Together with ServiceLoader optimization in R8
-keep class com.huya.mobile.uinspector.** { *; }
-keep class * implements com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.properties.layoutParam.LayoutParamsPropertiesParserService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.hierarchy.extra.HierarchyExtraInfoService { *; }

-assumenosideeffects class com.huya.mobile.uinspector.util.SpiUtilsKt {
    boolean ENABLE_FAST_SPI return false;
}