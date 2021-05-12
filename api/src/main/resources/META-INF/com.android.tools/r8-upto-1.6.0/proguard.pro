# When editing this file, update the following files as well:
# - META-INF/com.android.tools/proguard/proguard.pro
# - META-INF/proguard/proguard.pro
-keep class com.huya.mobile.uinspector.** { *; }
-keep class * implements com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.properties.layoutParam.LayoutParamsPropertiesParserService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.hierarchy.extra.HierarchyExtraInfoService { *; }