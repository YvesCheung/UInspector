# Uinspector

![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector.png)

> A UI inspector to traverse a view hierarchy on Android

![Build](https://github.com/YvesCheung/UInspector/workflows/Build/badge.svg) 
[![Jitpack](https://jitpack.io/v/YvesCheung/UInspector.svg)](https://jitpack.io/#YvesCheung/UInspector) 
[![Maven Central](https://img.shields.io/maven-central/v/io.github.yvescheung/Uinspector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yvescheung%22%20AND%20a:%22Uinspector%22)

[中文版Readme](https://github.com/YvesCheung/UInspector/blob/2.x/Readme-CN.md)

## Preview

|What are the properties| Where is the source code | Show the layout boundary |
| :---: | :---: | :---: |
|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/properties_preview.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/hierarchy_preview_new.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/targets_preview_new.jpeg)


|After selecting the view, you can see the line number of source code| Line 59 in the corresponding file is the source code where `Text` is located |
| :---: | :---: |
|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/inspectJetpackCompose.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/composeSourceCodeLineNumber.jpg)|

## Feature

- Don't have to endure the long loading with `Layoutinspector`
    ![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/response_time.jpg)
- Support extensions:
  - [X] Glide
  - [X] Fresco
  - [x] MultiType
  - [X] Lottie
  - [X] Jetpack compose

## Usage 

1. Start the inspector through the notification
2. Tap the view you want to inspect
3. Now you can see the properties on the popup panel
4.  **Uinspector intercept the 'single tap' event, but you can perform click on a View by double tap instead!** And the scroll event/ key event can be dispatched as usual.

![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector_preview.gif)

## Get Started

```groovy
repositories {
    mavenCentral()
}

dependencies {
    // debugImplementation because Uinspector should only run in debug builds.
    debugImplementation "io.github.yvescheung:Uinspector:x.y.z"
    
    // optional integration library
    debugImplementation "io.github.yvescheung:Uinspector-optional-viewmodel:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-fresco:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-glide:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-multitype:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-lottie:x.y.z"
    
    // optional integration library for Jetpack Compose!
    debugImplementation "io.github.yvescheung:Uinspector-optional-compose:x.y.z"
}
```
> x.y.z replace with [![Maven Central](https://img.shields.io/maven-central/v/io.github.yvescheung/Uinspector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yvescheung%22%20AND%20a:%22Uinspector%22)

**That’s it, there is no code change needed!**

## Optional Dependencies

- **Glide**

    If an image is loaded with [Glide](https://github.com/bumptech/glide), you can inspect the properties on the ImageView:

    <img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/glide.jpg" alt="Inspect ImageView with Glide" width="360">



    `glide model` : The image source, maybe an url or a resource id

    `glide error` : The error drawable

    `glide placeholder`: The place holder drawable
    
    All you need to do is Add the gradle dependency on the Glide integration library: 
    
    ```groovy
    dependencies {
        debugImplementation 'com.github.YvesCheung.UInspector:Uinspector-optional-glide:x.y.z'
    }
    ```

    > x.y.z replace with [![Maven Central](https://img.shields.io/maven-central/v/io.github.yvescheung/Uinspector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yvescheung%22%20AND%20a:%22Uinspector%22)

#### To see more optional dependencies below:

- [**Fresco**](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-fresco.md)
- [**Lottie**](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-lottie.md)
- [**Inspect your own custom view**](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-custom-view.md) 

## Develop

- You can develop your own panel and add it into `UInspector`:

    [See Doc](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-custom-panel.md)

- `UInspector` will launch automatically when the application starts. You can disable this feature if you don't want this:

    ```groovy
    dependencies {
         debugImplementation('com.github.YvesCheung.UInspector:Uinspector:x.y.z') {
             // After excluding, UInspector won't launch until you invoke it's `create` method!
             exclude module: 'Uinspector-optional-autoinstall'
         }
    }
    ```
  
- Development environment
    
    * Branch 2.x : Enable `Jetpack Compose` compiler feature.
    * Branch 1.x : Just Android View.

## Inspiration

- [FLEX](https://github.com/FLEXTool/FLEX)

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
