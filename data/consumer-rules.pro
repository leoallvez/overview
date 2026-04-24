# ProGuard rules for the :data module

# Keep generic type information
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, InnerClasses, EnclosingMethod

# Keep serializable models (Moshi and Kotlinx Serialization)
-keep @kotlinx.serialization.Serializable class ** { *; }
-keep class br.dev.singular.overview.data.model.** { *; }
-keep class br.dev.singular.overview.data.network.response.** { *; }

# Retrofit
-keep interface * {
    @retrofit2.http.* <methods>;
}

# NetworkResponseAdapter
-keep class com.haroldadmin.cnradapter.** { *; }

# Enums
-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
