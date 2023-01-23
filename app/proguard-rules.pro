# Add project specific ProGuard rules here.
##---------------BEGIN GSON RULES----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

##---------------END GSON RULES-------------

##---------------BEGIN MOSHI RULES----------
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
##---------------END MOSHI RULES-----------

##---------------BEGIN APP RULES-----------
-keepnames @kotlin.Metadata class br.com.deepbyte.overview.data.model.**
-keep class br.com.deepbyte.overview.data.model.** { <fields>; }
-keepnames @kotlin.Metadata class br.com.deepbyte.overview.data.api.response.**
-keep class br.com.deepbyte.overview.data.api.response.** { <fields>; }
##---------------END APP RULES-------------
