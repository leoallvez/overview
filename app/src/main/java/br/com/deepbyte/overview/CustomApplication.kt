package br.com.deepbyte.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import br.com.deepbyte.overview.util.CrashlyticsReportingTree
import br.com.deepbyte.overview.workers.StreamingDefaultSetupWorker
import br.com.deepbyte.overview.workers.StreamingUpdateWorker
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import io.github.leoallvez.firebase.CrashlyticsSource
import timber.log.Timber
import java.util.concurrent.TimeUnit
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
        initWorkers()
        initTimber()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun initWorkers() {
        scheduleStreamingDefaultTask()
        scheduleStreamingUpdateTask()
    }

    private fun scheduleStreamingDefaultTask() {
        val workerRequest = OneTimeWorkRequestBuilder<StreamingDefaultSetupWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workerRequest)
    }

    private fun scheduleStreamingUpdateTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workerRequest = OneTimeWorkRequestBuilder<StreamingUpdateWorker>()
            .setInitialDelay(duration = 1, TimeUnit.MINUTES)
            .setConstraints(constraints = constraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workerRequest)
    }

    private fun initTimber() = if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    } else {
        Timber.plant(CrashlyticsReportingTree(crashlytics))
    }
}
