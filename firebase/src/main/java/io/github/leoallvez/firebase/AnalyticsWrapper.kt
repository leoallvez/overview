package io.github.leoallvez.firebase

import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsSource {
    fun logEvent(event: AnalyticsEvent, param: String)
}

class AnalyticsWrapper(
    private val _firebaseAnalytics: FirebaseAnalytics?
) : AnalyticsSource {

    override fun logEvent(event: AnalyticsEvent, param: String) {
        _firebaseAnalytics?.setUserProperty(event.name, param)
    }
}