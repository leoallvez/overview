package br.dev.singular.overview.ui.media

import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaRepository
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.di.MainDispatcher
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.ui.screens.common.UiEvent
import br.dev.singular.overview.presentation.ui.screens.common.UiEvents
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.ui.AdViewModel
import br.dev.singular.overview.ui.MediaUiState
import br.dev.singular.overview.ui.model.toDomain
import br.dev.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    adsManager: RemoteConfig<Boolean>,
    private val _uiEvents: UiEvents,
    private val _mediaRepository: IMediaRepository,
    private val queryStateUseCase: ICatalogQueryStateUseCase,
    @param:MainDispatcher private val _dispatcher: CoroutineDispatcher
) : AdViewModel(adsManager) {

    private val _uiState = MutableStateFlow<MediaUiState>(UiState.Loading())
    val uiState: StateFlow<MediaUiState> = _uiState

    fun load(apiId: Long, type: MediaType) {
        _uiState.value = UiState.Loading()
        viewModelScope.launch(_dispatcher) {
            _mediaRepository.getItem(apiId, type).collect { result ->
                _uiState.value = result.toUiState()
            }
        }
    }

    fun saveSelectedStream(streaming: StreamingEntity) {
        viewModelScope.launch(_dispatcher) {
            val query = queryStateUseCase.get() ?: QueryState()
            queryStateUseCase.save(query.copy(catalog = streaming.toDomain()))
        }
    }

    fun updateLike(media: Media?, isLiked: Boolean) {
        viewModelScope.launch(_dispatcher) {
            media?.let {
                it.isLiked = isLiked
                _mediaRepository.update(media)
                _uiEvents.trigger(UiEvent.ReloadFavorites)
            }
        }
    }
}
