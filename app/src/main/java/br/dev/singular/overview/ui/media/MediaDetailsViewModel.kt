package br.dev.singular.overview.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.data.repository.media.interfaces.IMediaRepository
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.ui.MediaUiState
import br.dev.singular.overview.ui.UiState
import br.dev.singular.overview.util.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IMediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaUiState>(UiState.Loading())
    val uiState: StateFlow<MediaUiState> = _uiState

    private val _dataNotLoaded
        get() = (_uiState.value is UiState.Success).not()

    fun loadMediaDetails(apiId: Long, type: MediaTypeEnum) = viewModelScope.launch {
        if (_dataNotLoaded) {
            _repository.getItem(apiId, type).collect { result ->
                _uiState.value = result.toUiState()
            }
        }
    }

    fun refresh(apiId: Long, type: MediaTypeEnum) {
        _uiState.value = UiState.Loading()
        loadMediaDetails(apiId, type)
    }
}
