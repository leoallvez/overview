package io.github.leoallvez.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsSource {
    fun logEvent(event: AnalyticsEvent, bundle: Bundle)
}

class AnalyticsWrapper(
    private val _firebaseAnalytics: FirebaseAnalytics?
) : AnalyticsSource {

    override fun logEvent(event: AnalyticsEvent, bundle: Bundle) {
        _firebaseAnalytics?.logEvent(event.name, bundle)
    }
}