package io.github.leoallvez.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics

interface CrashlyticsSource {
    fun recordException(throwable: Throwable)
    fun log(message: String)
}

class CrashlyticsWrapper(
    private val _firebaseCrashlytics: FirebaseCrashlytics
) : CrashlyticsSource {

    override fun recordException(throwable: Throwable) {
        _firebaseCrashlytics.recordException(throwable)
    }

    override fun log(message: String) {
        _firebaseCrashlytics.log(message)
    }
}
