package io.github.leoallvez.take

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startLog()
    }

    private fun startLog() {
        if (BuildConfig.DEBUG) {
            //TODO: create Timber Tree classes to get Crashlytics logs.
            Timber.plant(Timber.DebugTree())
        }
    }
}
