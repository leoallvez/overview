package br.com.deepbyte.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.deepbyte.overview.util.CrashlyticsReportingTree
import br.com.deepbyte.overview.work.StreamingDefaultSetupWork
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import io.github.leoallvez.firebase.CrashlyticsSource
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CustomApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var crashlytics: CrashlyticsSource

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        initWorker()
        initTimber()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun initWorker() {
        val request = OneTimeWorkRequestBuilder<StreamingDefaultSetupWork>().build()
        WorkManager.getInstance(applicationContext).enqueue(request)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsReportingTree(crashlytics))
        }
    }
}
