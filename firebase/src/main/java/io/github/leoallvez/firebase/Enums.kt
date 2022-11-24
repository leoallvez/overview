package io.github.leoallvez.firebase

import com.google.firebase.analytics.FirebaseAnalytics

enum class RemoteConfigKey(val value: String) {
    DISPLAY_ADS_KEY(value = "display_ads"),
    SUGGESTIONS_LIST_KEY(value = "suggestions_list"),
    FIREBASE_ENVIRONMENT(value = "firebase_environment")
}

enum class AnalyticsEvent(val value: String) {
    OPEN_SCREEN(value = "open_screen"),
    EXIT_SCREEN(value = "exit_screen"),
}

enum class AnalyticsParam(val value: String) {
    SCREEN_NAME(value = FirebaseAnalytics.Param.SCREEN_NAME)
}
