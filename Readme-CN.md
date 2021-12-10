# Uinspector

![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector.png)

> 一个帮助Android应用检阅Ui的工具


![Build](https://github.com/YvesCheung/UInspector/workflows/Build/badge.svg) [![Jitpack](https://jitpack.io/v/YvesCheung/UInspector.svg)](https://jitpack.io/#YvesCheung/UInspector) [![Maven Central](https://img.shields.io/maven-central/v/io.github.yvescheung/Uinspector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yvescheung%22%20AND%20a:%22Uinspector%22)

## 预览

|检阅属性| 源码定位 | 布局边界 |
| :---: | :---: | :---: |
|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/properties_preview.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/hierarchy_preview_new.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/targets_preview_new.jpeg)


| 手指选中元素后，可以看到Compose方法的源码行数 | 对应源码第59行正是Text所在的代码 |
| :---: | :---: |
|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/inspectJetpackCompose.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/composeSourceCodeLineNumber.jpg)|

## 特性

- 不用忍受 `LayoutInspector` 漫长的加载
    ![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/response_time.jpg)
- 支持扩展第三方库的属性:
  - [X] Glide
  - [X] Fresco
  - [x] MultiType
  - [X] Lottie
  - [X] Jetpack compose

## 使用 

1. 从通知栏点击开启 `Uinspector`
2. 点击屏幕上的 `View`
3. 现在可以从出现的面板上看到 `View` 的属性
4. `Uinspector` 会消费单击事件，可以用双击来触发原本单击的行为。滑动等其他交互则不受影响。

![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector_preview.gif)

## 依赖

```groovy
repositories {
    mavenCentral()
}

dependencies {
    // debugImplementation是因为只在debug构建中使用
    debugImplementation "io.github.yvescheung:Uinspector:x.y.z"
    
    // 可选的集成库
    debugImplementation "io.github.yvescheung:Uinspector-optional-viewmodel:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-fresco:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-glide:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-multitype:x.y.z"
    debugImplementation "io.github.yvescheung:Uinspector-optional-lottie:x.y.z"
    
    // 可选的支持Jetpack Compose!
    debugImplementation "io.github.yvescheung:Uinspector-optional-compose:x.y.z"
}
```
> x.y.z 代替为 [![Maven Central](https://img.shields.io/maven-central/v/io.github.yvescheung/Uinspector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yvescheung%22%20AND%20a:%22Uinspector%22)

**接入完成! 无需写任何代码！**

## 支持第三方库

- **Glide**

    如果图片是使用 [Glide](https://github.com/bumptech/glide) 加载的, 你可以点击 `ImageView` 检阅对应的属性:

    <img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/glide.jpg" alt="Inspect ImageView with Glide" width="360">



    `glide model` : 图片来源：可能是url或者resId

    `glide error` : 加载错误时的占位图

    `glide placeholder`: 加载完成前的占位图
    
    为了可以支持检阅 `Glide` 的属性， 你所需要做的就是添加对应的集成库依赖: 
    
    ```groovy
    dependencies {
        debugImplementation 'com.github.YvesCheung.UInspector:Uinspector-optional-glide:x.y.z'
    }
    ```

    > x.y.z replace with [![Maven Central](https://img.shields.io/maven-central/v/io.github.yvescheung/Uinspector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.yvescheung%22%20AND%20a:%22Uinspector%22)

#### 其他的一些集成依赖库说明:

- [**Fresco**](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-fresco.md)
- [**Lottie**](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-lottie.md)
- [**添加你自定义的面板到Uinspector中**](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-custom-view.md) 

## 开发环境

- 你可以添加自己的面板或者扩展属性到 `UInspector` 中:

    [点此查看](https://github.com/YvesCheung/UInspector/blob/2.x/docs/uinspector-optional-custom-panel.md)

- 在应用启动时， `UInspector` 会自动显示到通知栏上。你可以选择禁用这个特性:

    ```groovy
    dependencies {
         debugImplementation('com.github.YvesCheung.UInspector:Uinspector:x.y.z') {
             // After excluding, UInspector won't launch until you invoke it's `create` method!
             exclude module: 'Uinspector-optional-autoinstall'
         }
    }
    ```
  
- 开发特性
    
    * 分支 2.x : 开启 `Jetpack compose` 编译特性。
    * 分支 1.x : 只支持 `Android View`。


## 许可

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
