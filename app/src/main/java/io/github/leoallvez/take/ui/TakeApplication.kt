package io.github.leoallvez.take.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.leoallvez.take.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class TakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        //TODO: create Timber Tree classes to get Crashlytics logs.
    }
}