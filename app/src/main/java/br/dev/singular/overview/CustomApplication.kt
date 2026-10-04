package br.dev.singular.overview

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import androidx.work.Configuration.Provider
import br.dev.singular.overview.data.local.workers.WorkManagerFacade
import br.dev.singular.overview.data.remote.config.IRemoteConfigProvider
import br.dev.singular.overview.monitoring.CrashlyticsSource
import br.dev.singular.overview.presentation.tagging.TagManager
import br.dev.singular.overview.presentation.ui.screens.common.UiEvent
import br.dev.singular.overview.presentation.ui.screens.common.UiEvents
import br.dev.singular.overview.util.CrashlyticsReportingTree
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
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
    lateinit var uiEvents: UiEvents

    private val workerFacade: WorkManagerFacade by lazy {
        WorkManagerFacade(_context = applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        remoteConfig.start()
        MobileAds.initialize(this)
        TagManager.init(instance = FirebaseAnalytics.getInstance(this))
        workerFacade.init()
        initFavoritesSync()
        initTimber()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    // The favorites are synced whenever the app comes to the foreground, which includes the
    // app start, so changes made on other devices show up without restarting the process.
    private fun initFavoritesSync() {
        val processOwner = ProcessLifecycleOwner.get()
        processOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) = workerFacade.syncFavorites()
        })
        processOwner.lifecycleScope.launch {
            workerFacade.observeFavoritesChanged().collect {
                uiEvents.trigger(UiEvent.ReloadFavorites)
            }
        }
    }

    private fun initTimber() = Timber.plant(
        tree = if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            CrashlyticsReportingTree(crashlyticsSource = crashlytics)
        }
    )
}
