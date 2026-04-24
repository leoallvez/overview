package br.dev.singular.overview.presentation.ui.screens.catalog.details

import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.usecase.ICatalogQueryStateUseCase
import br.dev.singular.overview.domain.usecase.media.IGetRemoteMediasUseCase
import br.dev.singular.overview.presentation.model.CatalogUiModel
import br.dev.singular.overview.presentation.model.GenreUiModel
import br.dev.singular.overview.presentation.model.MediaUiType
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.catalog.details.interaction.CatalogDetailIntent
import br.dev.singular.overview.presentation.ui.screens.pagination.BaseMediaPagingViewModel
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogDetailsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getMediasUseCase: IGetRemoteMediasUseCase,
    private val queryStateUseCase: ICatalogQueryStateUseCase
) : BaseMediaPagingViewModel() {

    init {
        viewModelScope.launch(dispatcher) {
            queryStateUseCase.observe().collect { savedQuery ->
                savedQuery?.toUi()?.let {
                    syncQueryState(it)
                }
            }
        }
    }

    private fun syncQueryState(savedQuery: QueryUiState) {
        if (savedQuery != queryState.value) {
            onQueryChanged(savedQuery)
        }
    }

    fun handleIntent(intent: CatalogDetailIntent) = with(intent) {
        when (this) {
            is CatalogDetailIntent.SelectType -> onSelectType(type)
            is CatalogDetailIntent.SelectGenre -> onSelectGenre(genre)
            is CatalogDetailIntent.SelectCatalog -> onSelectCatalog(catalog)
            is CatalogDetailIntent.ChangeScrollState -> onSetScrollState(scrollState)
        }
    }

    override suspend fun onFetching(query: QueryUiState) =
        getMediasUseCase.invoke(query = query.toDomain())

    private fun onSelectType(type: MediaUiType) =
        onUpdateQuery(query = queryState.value.copy(type = type, genre = null))

    private fun onSelectGenre(genre: GenreUiModel) =
        onUpdateQuery(query = queryState.value.copy(genre = genre))

    private fun onSelectCatalog(catalog: CatalogUiModel) =
        onUpdateQuery(query = queryState.value.copy(catalog = catalog))

    private fun onUpdateQuery(query: QueryUiState) {
        onQueryChanged(query)
        saveQueryState()
    }

    private fun saveQueryState() = viewModelScope.launch(dispatcher) {
        queryStateUseCase.save(queryState.value.toDomain())
    }
}
