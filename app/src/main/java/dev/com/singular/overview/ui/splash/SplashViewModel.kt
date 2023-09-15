package dev.com.singular.overview.ui.splash

import androidx.lifecycle.ViewModel
import dev.com.singular.overview.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    val remoteConfig: RemoteSource
) : ViewModel()
