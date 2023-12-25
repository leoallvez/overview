package br.dev.singular.overview.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import br.dev.singular.overview.di.IoDispatcher
import br.dev.singular.overview.util.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.firebase.RemoteSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val remoteConfig: RemoteSource,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: ISelectedStreamingRepository,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _selectedStreamingJson: String = ""

    init {
        viewModelScope.launch(_dispatcher) {
            _repository.getSelectedItem().collect {
                _selectedStreamingJson = it.toJson()
            }
        }
    }

    fun getSelectedStreamingJson() = _selectedStreamingJson
}
