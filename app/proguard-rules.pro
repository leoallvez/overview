# R8 Optimizations - Corrected version for Moshi, Retrofit, and Generics

# 1. Essential attributes for Reflection and Generic Types
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, InnerClasses, EnclosingMethod, AnnotationDefault

# 2. Moshi
# The error "Platform class java.util.ArrayList ... requires explicit JsonAdapter"
# occurs when R8 removes generic signatures from data classes,
# causing Moshi to think it should serialize a raw ArrayList (no type).
-keepclassmembers class ** {
    @com.squareup.moshi.Json *;
}
# Keep all classes that Moshi uses for serialization
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# 3. Kotlinx Serialization (used in other modules)
-keep @kotlinx.serialization.Serializable class ** { *; }
-keepclassmembers class **$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

# 4. Retrofit and API Interfaces
# Important: DO NOT obfuscate the names of model classes used in Retrofit interfaces
-keep interface * {
    @retrofit2.http.* <methods>;
}

# 5. Keep model classes (Data Models)
# Since the project uses Moshi by reflection (no CodeGen seen in build.gradle),
# we need to keep the fields and constructors of model classes.
-keep class br.dev.singular.overview.data.model.** { *; }
-keep class br.dev.singular.overview.data.api.response.** { *; }

# 6. NetworkResponseAdapter
-keep class com.haroldadmin.cnradapter.** { *; }

# 7. Enums
-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 8. Log removal in Release
-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# 9. Debugging
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
