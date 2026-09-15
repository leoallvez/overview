package br.dev.singular.overview.presentation.ui.screens.favorites

import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaType
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IObserveFavoritesUseCase
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.pagination.BaseMediaPagingViewModel
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    useCase: IObserveFavoritesUseCase
) : BaseMediaPagingViewModel() {

    private val favorites = MutableStateFlow<List<Media>?>(null)

    init {
        viewModelScope.launch(dispatcher) {
            useCase()
                .catch { e ->
                    Timber.e(e)
                    emit(emptyList())
                }
                .collect { onFavoritesChanged(it) }
        }
    }

    override suspend fun onFetching(query: QueryUiState): UseCaseState<Page<Media>> {
        val type = query.type.toDomain()
        val items = favorites.filterNotNull().first()
            .filter { type == MediaType.ALL || it.type == type }
        return UseCaseState.Success(Page(items, currentPage = query.page, isLastPage = true))
    }

    fun onSetType(type: MediaUiType) {
        onQueryChanged(query = queryState.value.copy(type = type))
    }

    private fun onFavoritesChanged(medias: List<Media>) {
        val previous = favorites.value
        favorites.value = medias
        if (previous != null && previous != medias) onReload()
    }
}
