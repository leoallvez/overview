package br.dev.singular.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import br.dev.singular.overview.data.source.workers.WorkManagerFacade
import br.dev.singular.overview.util.CrashlyticsReportingTree
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
