package br.dev.singular.overview.ui

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.core.remote.RemoteConfigProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DefaultViewModel @Inject constructor(
    val remoteConfig: RemoteConfigProvider,
    val analyticsTracker: IAnalyticsTracker
) : ViewModel()
