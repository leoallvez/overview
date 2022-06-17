package io.github.leoallvez.take

import android.os.Bundle
import io.github.leoallvez.firebase.AnalyticsEvent.SCREEN_VIEW
import io.github.leoallvez.firebase.AnalyticsParam.SCREEN_NAME
import io.github.leoallvez.firebase.AnalyticsSource
import timber.log.Timber
import javax.inject.Inject

class AnalyticsLogger @Inject constructor(
    private val _source: AnalyticsSource
) : Logger {
    override fun logScreenView(screenName: String) {
        val bundle = Bundle().apply { putString(SCREEN_NAME.value, screenName) }
        _source.logEvent(SCREEN_VIEW, bundle)
        Timber.tag(TAG).i(message = "start $screenName")
    }

    companion object {
        private const val TAG = "analytics_logger"
    }
}
