# R8 Optimizations - Optimized for Overview Project

# 1. Essential attributes for Reflection and Generic Types
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, InnerClasses, EnclosingMethod, AnnotationDefault

# 2. Log removal in Release
# This strips debug logs from the release build for better performance and security.
-assumenosideeffects class timber.log.Timber {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# 3. Debugging
# Keeps line numbers in stack traces while still allowing obfuscation.
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
