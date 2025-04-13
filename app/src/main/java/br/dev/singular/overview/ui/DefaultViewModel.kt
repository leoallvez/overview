package br.dev.singular.overview.ui

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.core.remote.RemoteSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DefaultViewModel @Inject constructor(
    val remoteConfig: RemoteSource,
    val analyticsTracker: IAnalyticsTracker
) : ViewModel()
