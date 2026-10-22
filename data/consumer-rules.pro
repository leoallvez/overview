# ProGuard rules for the :data module
# Most rules are provided by library dependencies (Retrofit, Kotlinx Serialization, etc.)
# or managed in the app module's proguard-rules.pro.

# Keep generic type information for the data layer
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, InnerClasses, EnclosingMethod

# R8 full mode strips the generic signature of non-kept classes, which prevents Retrofit from
# resolving the call adapter for suspend functions returning NetworkResponse.
-keep,allowobfuscation,allowshrinking class com.haroldadmin.cnradapter.NetworkResponse
