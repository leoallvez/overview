package br.com.deepbyte.take.ui.splash

import androidx.lifecycle.ViewModel
import br.com.deepbyte.take.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker
) : ViewModel()
