package br.dev.singular.overview

import android.os.Bundle
import br.dev.singular.overview.core.analytics.AnalyticsSource
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class AnalyticsTracker(
    private val source: AnalyticsSource
) : IAnalyticsTracker {

    override fun screenViewEvent(screenName: String, className: String) {
        val bundle = createScreenViewBundle(screenName, className)
        source.screenViewEvent(bundle)
        Timber.i(message = "$screenName $className")
    }

    private fun createScreenViewBundle(screenName: String, className: String) = Bundle().apply {
        putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        putString(FirebaseAnalytics.Param.SCREEN_CLASS, className)
    }
}
