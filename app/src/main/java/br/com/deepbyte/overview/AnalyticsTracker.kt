package br.com.deepbyte.overview

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import io.github.leoallvez.firebase.AnalyticsSource
import timber.log.Timber

class AnalyticsTracker(
    private val _source: AnalyticsSource
) : IAnalyticsTracker {

    override fun screenViewEvent(screenName: String, className: String) {
        val bundle = createScreenViewBundle(screenName, className)
        _source.screenViewEvent(bundle)
        Timber.i(message = "$screenName $className")
    }

    private fun createScreenViewBundle(screenName: String, className: String) =
        Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, className)
        }

}
