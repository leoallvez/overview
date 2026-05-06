# ProGuard rules for the :presentation module

-keepattributes Signature, InnerClasses, EnclosingMethod

# Keep serializable models
-keep @kotlinx.serialization.Serializable class ** { *; }

# Enums (Used in UI and Mappings)
-keepclassmembers enum * {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
