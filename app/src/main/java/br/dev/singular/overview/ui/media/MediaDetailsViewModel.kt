package br.dev.singular.overview.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.repository.media.interfaces.IMediaRepository
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import br.dev.singular.overview.data.source.media.MediaTypeEnum
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.ui.MediaUiState
import br.dev.singular.overview.ui.UiState
import br.dev.singular.overview.util.fromJson
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _mediaRepository: IMediaRepository,
    private val _streamingRepository: ISelectedStreamingRepository,
    @MainDispatcher private val _dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaUiState>(UiState.Loading())
    val uiState: StateFlow<MediaUiState> = _uiState

    private val _dataNotLoaded
        get() = (_uiState.value is UiState.Success).not()

    fun loadMediaDetails(apiId: Long, type: MediaTypeEnum) = viewModelScope.launch {
        if (_dataNotLoaded) {
            _mediaRepository.getItem(apiId, type).collect { result ->
                _uiState.value = result.toUiState()
            }
        }
    }

    fun refresh(apiId: Long, type: MediaTypeEnum) {
        _uiState.value = UiState.Loading()
        loadMediaDetails(apiId, type)
    }

    fun saveSelectedStream(streamingJson: String?) {
        viewModelScope.launch(_dispatcher) {
            _streamingRepository.updateSelected(streamingJson?.fromJson())
        }
    }

    fun updateLike(media: Media?, isLiked: Boolean) {
        viewModelScope.launch(_dispatcher) {
            media?.let {
                media.isLiked = isLiked
                _mediaRepository.updateLike(media.toMediaEntity())
            }
        }
    }
}
