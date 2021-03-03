# Uinspector-Optional-Lottie

## Introduce 

**Uinspector-Optional-Lottie** is an intergration library for [Uinspector](https://github.com/YvesCheung/UInspector) and [Lottie](https://github.com/airbnb/lottie-android).
Lottie is a mobile library that parses Adobe After Effects animations exported as json.
Uinspector-Optional-Lottie helps us to inspect the properties of `LottieAnimationView`!

## Get Started

1. Add dependencies to your `build.gradle` file

    ```groovy
    dependencies {
        //the framework of Uinspector
        debugImplementation 'com.pitaya.mobile:Uinspector:x.y.z'
        //the optional library to inspect properties of Lottie
        debugImplementation 'com.pitaya.mobile:Uinspector-optional-lottie:x.y.z'
    }
    ```
    
    > x.y.z replace with [![Download](https://api.bintray.com/packages/yvescheung/maven/UInspector/images/download.svg)](https://bintray.com/yvescheung/maven/UInspector/_latestVersion)


2. Run your app and turn on the Uinspector. Click the `LottieAnimationView`
 
    <img src="https://raw.githubusercontent.com/YvesCheung/UInspector/2.x/art/lottie.jpg" alt="Inspect Lottie" width="360">
    
    Now you can see the properties listed in the popupPanel:
    
    - `lottie fileName` : The json/zip file if exist
    - `lottie rawRes` : The resource id if exist
    - `lottie speed` : The speed of playing animation
    - `lottie duration` : How long the animation is
    - `lottie repeat mode` : The repeat mode of the animation
    - `lottie repeat count` : The repeat count of the animation