package br.com.deepbyte.overview.ui.splash

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    val remoteConfig: RemoteSource
) : ViewModel()
