# 丢弃LayoutInspector

![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/layoutinspector_no_device.jpg)

自从AS升级到4.x后，你的LayoutInspector会不会经常和我一样

**明明连着设备，却检测不到对应的进程？**

**检阅速度非常慢，直到超时都没有出画面？**

**勾选Live Update后，画面延迟成了PPT，最后放弃Live Update？**

终于，我放弃了LayoutInspector，选用更轻量的[Uinspector](https://github.com/YvesCheung/UInspector)来辅助完成一些基本的ui检查。

## Uinsepctor是什么？

`Uinspector` 是一个集成在Android应用内的ui检阅工具。

开启 `Uinspector` 后，通过点击屏幕上的元素来选取要检阅的目标，弹出的面板中可以查看目标 `View` 的布局和属性。

|查看View的大小/边距/基本属性| 查找目标所在的Activity/Fragment，快速定位所在代码 | 
| :---: | :---: |
|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/properties_preview.jpeg)|![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/hierarchy_preview.jpeg)|

## 相比起LayoutInspector有什么优势？

### 1. 轻便快速

  从手机的**通知栏**打开Inspector，直接**点击屏幕**就可以看到目标属性！
  <Br/>从通知栏点击关闭即可停止检阅，用完即走。
  
  ![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/uinspector_preview.gif)
  
### 2. 追踪动画

相比起幻灯片级的“Live Update”，`Uinspector` 直接运行在你的应用进程中，可以通过监听 `View` 的变化实时作出反馈，追踪布局/属性的变化，甚至是动画。

![](https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/trace_animator.gif)

### 3. 支持添加你的自定义View和自定义属性

`Uinspector` 可以集成其他第三方库，查看他们的特有属性：

|查看Glide的图片源|查看Fresco的图片源|查看Lottie的动画属性|
| :----:|:-----:|:-----:|
|<img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/glide.jpg" alt="Inspect Glide" height="360"/>|<img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/fresco.jpg" alt="Inspect Glide" height="360"/>|<img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/lottie.jpg" alt="Inspect Glide" height="360"/>|

当图片资源加载异常时，你可以点击图片地址用浏览器打开，快速确认是客户端加载问题还是图片资源本身的问题！

大部分的自定义 `View` 属性都非常丰富，使用 `LayoutInspector` 都是无法直接查看的。

而利用 `Uinspector` 加上你自己的集成库，则可以方便的支持这些属性。[第三方集成库文档](https://github.com/YvesCheung/UInspector/blob/master/docs/uinspector-optional-custom-view.md)。

甚至这个弹出面板的样式和内容不满足你的需求，也可以[添加你自己开发的面板进去](https://github.com/YvesCheung/UInspector/blob/master/Readme.md#develop/)。

## 如何使用Uinsepctor？

只需要在项目的 `build.gradle` 文件中添加一句：

```groovy
dependencies {
    debugImplementation 'com.pitaya.mobile:Uinspector:1.0.5'
}
```

**就可以了！** 如果不需要自定义开发的话，无需添加任何代码。

可选地，也可以根据需要添加一些第三方集成库：

```groovy
dependencies {
    debugImplementation 'com.pitaya.mobile:Uinspector-optional-glide:1.0.5'
    debugImplementation 'com.pitaya.mobile:Uinspector-optional-fresco:1.0.5'
    debugImplementation 'com.pitaya.mobile:Uinspector-optional-lottie:1.0.5'
}
```

**需要注意的是：**

打开 `Uinspector` 后，依然可以正常对应用进行手势操作，比如滑动屏幕，back键返回等等。
<Br/>但是，**单击事件会被消费，成为选中检阅目标的操作**！
<Br/>在这种模式下，可以通过**双击**来代替原来的点击，来触发原来`onClickListener`的操作！


[源码地址： https://github.com/YvesCheung/UInspector](https://github.com/YvesCheung/UInspector)