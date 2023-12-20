package br.dev.singular.overview.ui.streaming.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.repository.streaming.IStreamingRepository
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.ui.StreamingUiState
import br.dev.singular.overview.ui.UiState
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectStreamingModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IStreamingRepository,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<StreamingUiState>(UiState.Loading())
    val uiState: StateFlow<StreamingUiState> = _uiState

    init {
        loadUiState()
    }

    fun refresh() = loadUiState()

    private fun loadUiState() {
        viewModelScope.launch(_mainDispatcher) {
            _repository.getStreamingsData().collect { streams ->
                _uiState.value = streams.toUiState { streams.isNotEmpty() }
            }
        }
    }
}