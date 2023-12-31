package br.dev.singular.overview.ui.splash

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val remoteConfig: RemoteSource,
    val analyticsTracker: IAnalyticsTracker
) : ViewModel()
