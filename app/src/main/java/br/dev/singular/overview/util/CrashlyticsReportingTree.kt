package br.dev.singular.overview.util

import android.util.Log
import br.dev.singular.overview.core.crashlytics.CrashlyticsSource
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
