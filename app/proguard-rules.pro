# Add project specific ProGuard rules here.

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
#-keepnames @kotlin.Metadata class br.com.deepbyte.overview.data.model.**
-keep class br.com.deepbyte.overview.data.model.** { <fields>; }
#-keepnames @kotlin.Metadata class br.com.deepbyte.overview.data.api.response.**
-keep class br.com.deepbyte.overview.data.api.response.** { <fields>; }
##---------------END APP RULES-------------
