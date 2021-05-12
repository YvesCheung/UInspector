# Allow R8 to optimize away the FastServiceLoader.
# Together with ServiceLoader optimization in R8
-assumenosideeffects class com.huya.mobile.uinspector.util.SpiUtilsKt {
    boolean ENABLE_FAST_SPI return false;
}