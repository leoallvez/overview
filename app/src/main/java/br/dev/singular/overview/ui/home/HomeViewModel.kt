package br.dev.singular.overview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.HomeData
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.interfaces.IMediaCacheRepository
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.ui.HomeUiState
import br.dev.singular.overview.ui.UiState
import br.dev.singular.overview.ui.UiState.Success
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import br.dev.singular.overview.data.repository.streaming.IStreamingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _mediaRepository: IMediaCacheRepository,
    private val _streamingRepository: IStreamingRepository,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(UiState.Loading())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadUiState()
    }

    fun refresh() = loadUiState()

    private fun loadUiState() {
        viewModelScope.launch(_mainDispatcher) {
            _streamingRepository.getStreamingsData().collect { streams ->
                _uiState.value = HomeData(streams).toUiState { streams.isNotEmpty() }
            }

            _mediaRepository.getMediaCache().collect { medias ->
                setMediasInUiState(medias)
            }
        }
    }

    private fun setMediasInUiState(medias: List<MediaEntity>) {
        (_uiState.value as? Success)?.apply {
            _uiState.value = data.copy(recommendedMedias = medias).toUiState()
        }
    }
}
