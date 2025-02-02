package br.dev.singular.overview.ui

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Inject

@HiltViewModel
class DefaultViewModel @Inject constructor(
    val remoteConfig: RemoteSource,
    val analyticsTracker: IAnalyticsTracker
) : ViewModel()
