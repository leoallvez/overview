# ProGuard rules for the :data module
# Most rules are provided by library dependencies (Retrofit, Kotlinx Serialization, etc.)
# or managed in the app module's proguard-rules.pro.

# Keep generic type information for the data layer
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, InnerClasses, EnclosingMethod
