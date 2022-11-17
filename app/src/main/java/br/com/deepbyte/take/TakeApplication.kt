package br.com.deepbyte.take

import android.app.Application
import br.com.deepbyte.take.util.CrashlyticsReportingTree
import dagger.hilt.android.HiltAndroidApp
import io.github.leoallvez.firebase.CrashlyticsSource
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TakeApplication : Application() {

    @Inject
    lateinit var crashlyticsSource: CrashlyticsSource

    override fun onCreate() {
        super.onCreate()
        startLog()
    }

    private fun startLog() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsReportingTree(crashlyticsSource))
        }
    }
}
