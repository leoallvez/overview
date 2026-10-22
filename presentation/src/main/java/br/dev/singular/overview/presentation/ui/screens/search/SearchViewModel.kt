package br.dev.singular.overview.presentation.ui.screens.search

import androidx.lifecycle.viewModelScope
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.media.IGetRemoteMediasUseCase
import br.dev.singular.overview.domain.usecase.suggestion.IGetAllSuggestionsUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.di.domain.SearchMediaUseCase
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.model.QueryUiState
import br.dev.singular.overview.presentation.ui.screens.pagination.BaseMediaPagingViewModel
import br.dev.singular.overview.presentation.ui.screens.search.interaction.SearchIntent
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUi
import br.dev.singular.overview.presentation.ui.utils.mappers.domainToUi.toUiState
import br.dev.singular.overview.presentation.ui.utils.mappers.uiToDomain.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias SuggestionUIState = UiState<Map<String, List<MediaUiModel>>>

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    @param:SearchMediaUseCase
    private val mediasUseCase: IGetRemoteMediasUseCase,
    private val suggestionsUseCase: IGetAllSuggestionsUseCase
) : BaseMediaPagingViewModel() {

    private val _suggestionsState = MutableStateFlow<SuggestionUIState>(UiState.Loading())
    val suggestionsState = _suggestionsState.asStateFlow()

    override suspend fun onFetching(query: QueryUiState): UseCaseState<Page<Media>> {
        return mediasUseCase.invoke(query = query.toDomain())
    }

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.LoadSuggestions -> onLoadSuggestions()
            is SearchIntent.Search -> onQueryChanged(queryState.value.copy(query = intent.query))
            is SearchIntent.SetType -> onQueryChanged(queryState.value.copy(type = intent.type))
        }
    }

    private fun onLoadSuggestions() = viewModelScope.launch(dispatcher) {
        suggestionsUseCase.invoke().toUiState { it.toUi() }.let {
            _suggestionsState.value = it
        }
    }
}
