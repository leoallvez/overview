package br.dev.singular.overview.ui.streaming.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.data.local.source.CacheDataSource
import br.dev.singular.overview.data.local.source.CacheDataSource.Companion.KEY_SHOW_HIGHLIGHT_STREAMING_ICON
import br.dev.singular.overview.data.model.provider.StreamingData
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.repository.streaming.StreamingRepository
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.ui.StreamingUiState
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectStreamingViewModel @Inject constructor(
    private val _cache: CacheDataSource,
    private val _repository: StreamingRepository,
    @param:MainDispatcher private val _dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<StreamingUiState>(UiState.Loading())
    val uiState: StateFlow<StreamingUiState> = _uiState

    private lateinit var _streamingData: StreamingData

    init {
        viewModelScope.launch(_dispatcher) {
            _cache.setValue(KEY_SHOW_HIGHLIGHT_STREAMING_ICON, false)
            _streamingData = _repository.getAllLocal().first()
            loadUiState()
        }
    }

    fun refresh() = viewModelScope.launch(_dispatcher) {
        loadUiState()
    }

    fun saveSelectedStreaming(streaming: StreamingEntity) = viewModelScope.launch(_dispatcher) {
        updateUiState(selectedId = streaming.apiId)
        _repository.updateSelected(streaming)
    }

    private suspend fun loadUiState() {
        updateUiState(selectedId = _repository.getSelectedItem().first()?.apiId ?: 0)
    }

    private fun updateUiState(selectedId: Long) {
        _streamingData = _streamingData.copy(selectedId = selectedId)
        _uiState.value = _streamingData.toUiState { _streamingData.list.isNotEmpty() }
    }
}
