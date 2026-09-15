package br.dev.singular.overview.presentation.ui.screens.media.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
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

    private val _loginRequired = MutableStateFlow(false)
    val loginRequired: StateFlow<Boolean> = _loginRequired.asStateFlow()

    private val _favoriteAdded = MutableStateFlow(false)
    val favoriteAdded: StateFlow<Boolean> = _favoriteAdded.asStateFlow()

    fun handleIntent(intent: MovieDetailsIntent) {
        viewModelScope.launch(dispatcher) {
            when (intent) {
                is MovieDetailsIntent.Load -> onLoad(intent.id)
                is MovieDetailsIntent.Like -> onLike(intent.media)
                is MovieDetailsIntent.DismissLoginAlert -> _loginRequired.update { false }
                is MovieDetailsIntent.DismissFavoriteAdded -> _favoriteAdded.update { false }
                is MovieDetailsIntent.SelectCatalog -> {
                    delegate.selectCatalog(intent.catalog)
                }
            }
        }
    }

    private suspend fun onLoad(id: Long) {
        _uiState.update { UiState.Loading() }
        val isLiked = delegate.getIsLiked(id, MediaType.MOVIE)
        val result = useCase(id).toUiStateNullable { it.toUi(isLiked) }
        _uiState.update { result }
    }

    private suspend fun onLike(media: MediaDetailsUiModel.Movie) {
        when (val result = delegate.toggleLike(media.toMediaDomain())) {
            is UseCaseState.Success -> {
                _uiState.update {
                    val updatedMetadata = media.metadata.copy(isLiked = result.data)
                    UiState.Success(media.copy(metadata = updatedMetadata))
                }
                _favoriteAdded.update { result.data }
            }
            is UseCaseState.Failure -> _loginRequired.update {
                result.type is FailType.Unauthorized
            }
        }
    }
}
