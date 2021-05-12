# Files in this directory will be ignored starting with Android Gradle Plugin 3.6.0+

# When editing this file, update the following files as well for AGP 3.6.0+:
# - META-INF/com.android.tools/proguard/proguard.pro
# - META-INF/com.android.tools/r8-upto-1.6.0/proguard.pro
-keep class com.huya.mobile.uinspector.** { *; }
-keep class * implements com.huya.mobile.uinspector.ui.panel.popup.UInspectorChildPanelService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.properties.view.ViewPropertiesParserService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.properties.layoutParam.LayoutParamsPropertiesParserService { *; }
-keep class * implements com.huya.mobile.uinspector.impl.hierarchy.extra.HierarchyExtraInfoService { *; }
