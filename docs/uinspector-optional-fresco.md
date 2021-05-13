# Uinspector-Optional-Fresco

## Introduce 

**Uinspector-Optional-Fresco** is an intergration library for [Uinspector](https://github.com/YvesCheung/UInspector) and [Fresco](https://github.com/facebook/fresco).
Fresco is a library for displaying images in Android applications. 
Uinspector-Optional-Fresco helps us to inspect the properties of `SimpleDraweeView`!

## Get Started

1. Add dependencies to your `build.gradle` file

    ```groovy
    dependencies {
        //the framework of Uinspector
        debugImplementation 'com.github.YvesCheung.UInspector:Uinspector:x.y.z'
        //the optional library to inspect properties of Fresco
        debugImplementation 'com.github.YvesCheung.UInspector:Uinspector-optional-fresco:x.y.z'
    }
    ```
    
    > x.y.z replace with [![Jitpack](https://jitpack.io/v/YvesCheung/UInspector.svg)](https://jitpack.io/#YvesCheung/UInspector)

2. Run your app and turn on the Uinspector. Click the `SimpleDraweeView`
 
    <img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/fresco.jpg" alt="Inspect Fresco" width="360">
    
    Now you can see the properties listed in the popupPanel:
    
    - `fresco src` : The image source set in SimpleDraweeView
    - `fresco src type` : The type of *fresco src*
    - `fresco decode` : The bitmap decode config used for this image
    - `fresco prefer width/height` : What size the image is going to download
    - `fresco rotation` : How the image should be rotated by Fresco