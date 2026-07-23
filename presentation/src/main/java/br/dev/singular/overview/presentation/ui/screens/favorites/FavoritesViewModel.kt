package br.dev.singular.overview.presentation.ui.screens.favorites


import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetAllLocalMediasUseCase
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.common.UiEvent
import br.dev.singular.overview.presentation.ui.screens.common.UiEvents
import br.dev.singular.overview.presentation.ui.screens.pagination.BaseMediaPagingViewModel
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    uiEvents: UiEvents,
    dispatcher: CoroutineDispatcher,
    private val useCase: IGetAllLocalMediasUseCase
) : BaseMediaPagingViewModel() {

    init {
        viewModelScope.launch(dispatcher) {
            uiEvents.stream
                .filterIsInstance<UiEvent.ReloadFavorites>()
                .collect {
                    onReload()
                }
        }
    }

    override suspend fun onFetching(query: QueryUiState): UseCaseState<Page<Media>> {
        return useCase.invoke(query = query.copy(isLiked = true).toDomain())
    }

    fun onSetType(type: MediaUiType) {
        onQueryChanged(query = queryState.value.copy(type = type))
    }
}
