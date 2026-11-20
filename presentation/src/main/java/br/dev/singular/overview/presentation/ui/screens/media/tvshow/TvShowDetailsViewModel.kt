package br.dev.singular.overview.presentation.ui.screens.media.tvshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetTvShowDetailsByIdUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaDetailsUiModel
import br.dev.singular.overview.presentation.ui.screens.media.IMediaDetailsDelegate
import br.dev.singular.overview.presentation.ui.screens.media.tvshow.interaction.TvShowDetailsIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUiStateNullable
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toMediaDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val delegate: IMediaDetailsDelegate,
    private val useCase: IGetTvShowDetailsByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MediaDetailsUiModel.TvShow?>>(UiState.Loading())
    val uiState: StateFlow<UiState<MediaDetailsUiModel.TvShow?>> = _uiState.asStateFlow()

    private val _loginRequired = MutableStateFlow(false)
    val loginRequired: StateFlow<Boolean> = _loginRequired.asStateFlow()

    private val _favoriteAdded = MutableStateFlow(false)
    val favoriteAdded: StateFlow<Boolean> = _favoriteAdded.asStateFlow()

    private var likedJob: Job? = null

    fun handleIntent(intent: TvShowDetailsIntent) {
        viewModelScope.launch(dispatcher) {
            when (intent) {
                is TvShowDetailsIntent.Load -> onLoad(intent.id)
                is TvShowDetailsIntent.Like -> onLike(intent.media)
                is TvShowDetailsIntent.DismissLoginAlert -> _loginRequired.update { false }
                is TvShowDetailsIntent.DismissFavoriteAdded -> _favoriteAdded.update { false }
                is TvShowDetailsIntent.SelectCatalog -> {
                    delegate.selectCatalog(intent.catalog)
                }
            }
        }
    }

    private suspend fun onLoad(id: Long) {
        _uiState.update { UiState.Loading() }
        val liked = delegate.observeIsLiked(id, MediaType.TV)
        val isLiked = liked.first()
        val result = useCase(id).toUiStateNullable { it.toUi(isLiked) }
        _uiState.update { result }
        observeIsLiked(liked)
    }

    private fun observeIsLiked(liked: Flow<Boolean>) {
        likedJob?.cancel()
        likedJob = viewModelScope.launch(dispatcher) {
            liked.collect { isLiked -> _uiState.update { it.withIsLiked(isLiked) } }
        }
    }

    private fun UiState<MediaDetailsUiModel.TvShow?>.withIsLiked(
        isLiked: Boolean
    ): UiState<MediaDetailsUiModel.TvShow?> {
        val media = (this as? UiState.Success)?.data ?: return this
        return UiState.Success(media.copy(metadata = media.metadata.copy(isLiked = isLiked)))
    }

    private suspend fun onLike(media: MediaDetailsUiModel.TvShow) {
        when (val result = delegate.toggleLike(media.toMediaDomain())) {
            is UseCaseState.Success -> _favoriteAdded.update { result.data }
            is UseCaseState.Failure -> _loginRequired.update {
                result.type is FailType.Unauthorized
            }
        }
    }
}
