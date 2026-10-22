package br.dev.singular.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Configuration.Provider
import br.dev.singular.overview.data.local.workers.CacheWorkScheduler
import br.dev.singular.overview.data.remoteconfig.IRemoteConfigProvider
import br.dev.singular.overview.monitoring.CrashlyticsSource
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.util.CrashlyticsReportingTree
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CustomApplication : Application(), Provider {

    @Inject
    lateinit var crashlytics: CrashlyticsSource

    @Inject
    lateinit var remoteConfig: IRemoteConfigProvider

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var cacheWorkScheduler: CacheWorkScheduler

    override fun onCreate() {
        super.onCreate()
        remoteConfig.start()
        MobileAds.initialize(this)
        TagManager.init(instance = FirebaseAnalytics.getInstance(this))
        cacheWorkScheduler.schedule()
        initTimber()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun initTimber() = Timber.plant(
        tree = if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashlyticsReportingTree(crashlyticsSource = crashlytics)
        }
    )
}
