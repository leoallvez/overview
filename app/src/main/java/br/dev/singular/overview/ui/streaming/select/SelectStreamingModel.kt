package br.dev.singular.overview.ui.streaming.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.repository.streaming.StreamingRepository
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.ui.StreamingUiState
import br.dev.singular.overview.ui.UiState
import br.dev.singular.overview.util.fromJson
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectStreamingModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: StreamingRepository,
    @MainDispatcher private val _dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<StreamingUiState>(UiState.Loading())
    val uiState: StateFlow<StreamingUiState> = _uiState

    init {
        loadUiState()
    }

    fun refresh() = loadUiState()

    private fun loadUiState() = viewModelScope.launch(_dispatcher) {
        val streams = _repository.getAllLocal().first()
        _uiState.value = streams.toUiState { streams.isNotEmpty() }
    }

    fun saveSelectedStream(streamJson: String?) = viewModelScope.launch(_dispatcher) {
        _repository.updateSelected(streamJson?.fromJson())
    }
}
