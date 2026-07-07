package br.dev.singular.overview.presentation.ui.screens.media.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.media.IGetMovieDetailsByIdUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.screens.media.IMediaDetailsDelegate
import br.dev.singular.overview.presentation.ui.screens.media.movie.interaction.MovieDetailsIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUiStateNullable
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toMediaDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val delegate: IMediaDetailsDelegate,
    private val useCase: IGetMovieDetailsByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MediaDetailsUiModel.Movie?>>(UiState.Loading())
    val uiState: StateFlow<UiState<MediaDetailsUiModel.Movie?>> = _uiState.asStateFlow()

    fun handleIntent(intent: MovieDetailsIntent) {
        viewModelScope.launch(dispatcher) {
            when (intent) {
                is MovieDetailsIntent.Load -> onLoad(intent.id)
                is MovieDetailsIntent.Like -> onLike(intent.media)
                is MovieDetailsIntent.SelectCatalog -> {
                    delegate.selectCatalog(intent.catalog)
                }
            }
        }
    }

    private suspend fun onLoad(id: Long) {
        _uiState.update { UiState.Loading() }
        val isLiked = delegate.getIsLiked(id)
        val result = useCase(id).toUiStateNullable { it.toUi(isLiked) }
        _uiState.update { result }
    }

    private suspend fun onLike(media: MediaDetailsUiModel.Movie) {

        val previousState = _uiState.value

        try {
            val isLiked = delegate.toggleLike(media.toMediaDomain())
            _uiState.update {
                val updatedMetadata = media.metadata.copy(isLiked = isLiked)
                UiState.Success(media.copy(metadata = updatedMetadata))
            }
        } catch (_: Exception) {
            _uiState.update { previousState }
        }
    }
}
