package br.com.deepbyte.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import br.com.deepbyte.overview.data.source.workers.StreamingDefaultSetupWorker
import br.com.deepbyte.overview.data.source.workers.StreamingSelectedUpdateWorker
import br.com.deepbyte.overview.util.CrashlyticsReportingTree
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
        val workerRequest = PeriodicWorkRequest
            .Builder(StreamingSelectedUpdateWorker::class.java, repeatInterval = 24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workerRequest)
    }

    private fun initTimber() = if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    } else {
        Timber.plant(CrashlyticsReportingTree(crashlytics))
    }
}
