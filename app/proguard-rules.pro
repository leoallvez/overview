# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Keep all @Serializable classes
-keep @kotlinx.serialization.Serializable class ** { *; }

# Keep the generated serializers
-keep class kotlinx.serialization.internal.** { *; }
-keep class kotlinx.serialization.json.** { *; }
-keepnames class kotlinx.serialization.** { *; }

# Prevent obfuscation of enum values
-keepclassmembers enum * { *; }

# Retain classes with reflective annotations
-keepclassmembers class ** {
    @kotlinx.serialization.* <fields>;
    @kotlinx.serialization.* <methods>;
}

# Keep all kotlinx serialization-related classes and methods
-keep class kotlinx.serialization.** { *; }