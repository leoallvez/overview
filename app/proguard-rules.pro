# Add project specific ProGuard rules here.
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

-keep class br.com.deepbyte.overview.data.model.** { <fields>; }
-keep class br.com.deepbyte.overview.data.api.response.** { <fields>; }

-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**