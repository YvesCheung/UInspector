# Uinspector

![](https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/uinspector.png)

> A UI inspector to traverse a view hierarchy on Android

![Build](https://github.com/YvesCheung/UInspector/workflows/Build/badge.svg) [![Download](https://api.bintray.com/packages/yvescheung/maven/UInspector/images/download.svg)](https://bintray.com/yvescheung/maven/UInspector/_latestVersion) [![Jitpack](https://jitpack.io/v/YvesCheung/UInspector.svg)](https://jitpack.io/#YvesCheung/UInspector) [![hackmd-github-sync-badge](https://hackmd.io/VtIqR5l1TEOCkU137kvRoQ/badge)](https://hackmd.io/VtIqR5l1TEOCkU137kvRoQ)

## Preview

|What are the properties| Where is the view | Select another view |
| :---: | :---: | :---: |
|![](https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/properties_preview.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/hierarchy_preview.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/targets_preview.jpeg)


## Feature

- Low intrusive, no code change required
- Turn on/off inspector throught the **notification**
- Select the target view by clicking on it
- **What's LayoutInspector can't do?**
    - Tracking animations
        
        ![](https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/trace_animator.gif)
        
    - Support to add your custom panel or custom view properties
        
        [See more](#Develop)

## Get Started

Add jcenter() to your project-level `build.gradle`

```groovy
allprojects {
    repositories {
        jcenter()
    }
}
```

Add dependency to your module-level `build.gradle`

```groovy
dependencies {
    ...
    // debugImplementation because Uinspector should only run in debug builds.
    debugImplementation 'com.huya.mobile:Uinspector:x.y.z'
}
```
> x.y.z replace with [![Download](https://api.bintray.com/packages/yvescheung/maven/UInspector/images/download.svg)](https://bintray.com/yvescheung/maven/UInspector/_latestVersion)

**That’s it, there is no code change needed!**

## Usage 

1. Start the inspector through the notification
2. Tap the view you want to inspect
3. Now you can see the properties on the popup panel

![](https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/uinspector_preview.gif)

**4. Uinspector intercept the 'single tap' event, but you can perform click on a View by double tap instead!** And the scroll event/ key event can be dispatched as usual.

## Optional Dependencies

- **Glide**

    If an image is loaded with [Glide](https://github.com/bumptech/glide), you can inspect the properties on the ImageView:

    <img src="https://raw.githubusercontent.com/YvesCheung/UInspector/master/art/glide.jpg" alt="Inspect ImageView with Glide" width="360">



    `glide model` : The image source, maybe an url or a resource id

    `glide error` : The error drawable

    `glide placeholder`: The place holder drawable
    
    All you need to do is Add the gradle dependency on the Glide integration library: 
    
    ```groovy
    dependencies {
        debugImplementation 'com.huya.mobile:Uinspector-optional-glide:x.y.z'
    }
    ```
    > x.y.z replace with [![Download](https://api.bintray.com/packages/yvescheung/maven/UInspector/images/download.svg)](https://bintray.com/yvescheung/maven/UInspector/_latestVersion)

#### To see more optional dependencies below:

- [**Fresco**](https://github.com/YvesCheung/UInspector/blob/master/docs/uinspector-optional-fresco.md)
- [**Lottie**](https://github.com/YvesCheung/UInspector/blob/master/docs/uinspector-optional-lottie.md)
- [**Inspect your own custom view**](https://github.com/YvesCheung/UInspector/blob/master/docs/uinspector-optional-custom-view.md)

## Develop

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

## Inspiration

- [FELX](https://github.com/FLEXTool/FLEX)

## License

	Copyright 2020 Yves Cheung
	
   	Licensed under the Apache License, Version 2.0 (the "License");
   	you may not use this file except in compliance with the License.
   	You may obtain a copy of the License at

       	http://www.apache.org/licenses/LICENSE-2.0

   	Unless required by applicable law or agreed to in writing, software
   	distributed under the License is distributed on an "AS IS" BASIS,
   	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   	See the License for the specific language governing permissions and
   	limitations under the License.
