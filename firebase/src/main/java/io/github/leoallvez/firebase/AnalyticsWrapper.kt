package io.github.leoallvez.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsSource {
    fun screenViewEvent(bundle: Bundle)
}

class AnalyticsWrapper(
    private val _firebaseAnalytics: FirebaseAnalytics?
) : AnalyticsSource {
    override fun screenViewEvent(bundle: Bundle) {
        _firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
