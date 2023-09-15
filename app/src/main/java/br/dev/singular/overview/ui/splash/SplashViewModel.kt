package br.dev.singular.overview.ui.splash

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import br.dev.singular.overview.IAnalyticsTracker
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    val remoteConfig: RemoteSource
) : ViewModel()
