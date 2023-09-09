package br.com.deepbyte.overview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.model.HomeData
import br.com.deepbyte.overview.data.model.media.MediaEntity
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaCacheRepository
import br.com.deepbyte.overview.data.repository.streaming.IStreamingRepository
import br.com.deepbyte.overview.di.MainDispatcher
import br.com.deepbyte.overview.di.ShowAds
import br.com.deepbyte.overview.ui.HomeUiState
import br.com.deepbyte.overview.ui.UiState
import br.com.deepbyte.overview.ui.UiState.Success
import br.com.deepbyte.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
            _streamingRepository.getStreamingsData().collect { steam ->
                _uiState.value = HomeData(streamingsData = steam).toUiState { steam.isNotEmpty() }
            }

            _mediaRepository.getMediaCache().collect { medias ->
                setMediasInUiState(medias = medias)
            }
        }
    }

    private fun setMediasInUiState(medias: List<MediaEntity>) {
        (_uiState.value as? Success)?.apply {
            _uiState.value = data.copy(recommendedMedias = medias).toUiState()
        }
    }
}
