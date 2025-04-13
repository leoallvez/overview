package br.dev.singular.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Configuration.Provider
import br.dev.singular.overview.core.crashlytics.CrashlyticsSource
import br.dev.singular.overview.data.source.workers.WorkManagerFacade
import br.dev.singular.overview.util.CrashlyticsReportingTree
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CustomApplication : Application(), Provider {

    @Inject
    lateinit var crashlytics: CrashlyticsSource

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val workerFacade: WorkManagerFacade by lazy {
        WorkManagerFacade(_context = applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        workerFacade.init()
        initTimber()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun initTimber() = if (BuildConfig.DEBUG) {
        Timber.plant(Timber.DebugTree())
    } else {
        Timber.plant(CrashlyticsReportingTree(crashlytics))
    }
}
