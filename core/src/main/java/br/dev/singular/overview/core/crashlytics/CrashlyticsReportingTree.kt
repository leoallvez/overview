package br.dev.singular.overview.core.crashlytics

import android.util.Log
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsReportingTree @Inject constructor(
    private val crashlyticsSource: CrashlyticsSource
) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR || priority == Log.WARN) {
            crashlyticsSource.log(message)
            t?.let {
                crashlyticsSource.recordException(it)
            }
        }
    }
}
