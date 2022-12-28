package br.com.deepbyte.overview

import android.os.Bundle
import io.github.leoallvez.firebase.AnalyticsEvent
import io.github.leoallvez.firebase.AnalyticsEvent.EXIT_SCREEN
import io.github.leoallvez.firebase.AnalyticsEvent.OPEN_SCREEN
import io.github.leoallvez.firebase.AnalyticsParam.SCREEN_NAME
import io.github.leoallvez.firebase.AnalyticsSource
import timber.log.Timber

class AnalyticsTracker(
    private val _source: AnalyticsSource
) : IAnalyticsTracker {

    override fun logOpenScreen(screenName: String) {
        log(event = OPEN_SCREEN, screenName = screenName)
    }

    override fun logExitScreen(screenName: String) {
        log(event = EXIT_SCREEN, screenName = screenName)
    }

    private fun log(event: AnalyticsEvent, screenName: String) {
        val bundle = Bundle().apply { putString(SCREEN_NAME.value, screenName) }
        _source.logEvent(event, bundle)
        Timber.i(message = "${event.value} $screenName")
    }
}