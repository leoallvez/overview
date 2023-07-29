package br.com.deepbyte.overview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.repository.streaming.IStreamingRepository
import br.com.deepbyte.overview.di.MainDispatcher
import br.com.deepbyte.overview.di.ShowAds
import br.com.deepbyte.overview.ui.StreamingUiState
import br.com.deepbyte.overview.ui.StreamingsWrap
import br.com.deepbyte.overview.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewHomeViewModel @Inject constructor(
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
            _repository.getItems().collect { streamings ->
                _uiState.value = streamings.toUiState()
            }
        }
    }

    private fun List<Streaming>.toUiState() = if (isNotEmpty()) {
        UiState.Success(data = toStreamingsWrap())
    } else {
        UiState.Error()
    }

    private fun List<Streaming>.toStreamingsWrap() =
        StreamingsWrap(selected = filter { it.selected }, unselected = filter { !it.selected })
}
