package br.dev.singular.overview.ui.splash

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.firebase.RemoteSource
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    val remoteConfig: RemoteSource
) : ViewModel() {
    fun getStreamingExploreJson() = MOCK_STREAMING_JSON

    companion object {
        const val MOCK_STREAMING_JSON = """{
            "display_priorities":{},
            "display_priority":1,
            "logo_path":"/t2yyOv40HZeVlLjYsCsPHnWLk4W.jpg",
            "provider_id":8,
            "provider_name":"Netflix",
            "selected":true
        }"""
    }
}
