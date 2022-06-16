package io.github.leoallvez.firebase

import com.google.firebase.analytics.FirebaseAnalytics

enum class RemoteConfigKey(val value: String) {
    DISPLAY_ADS_KEY(value = "display_ads"),
    SUGGESTIONS_LIST_KEY(value = "suggestions_list"),
}

enum class AnalyticsEvent(val value: String) {
    SCREEN_VIEW(value = FirebaseAnalytics.Event.SCREEN_VIEW)
}