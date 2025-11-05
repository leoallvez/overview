package br.dev.singular.overview.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaSearchPagingRepository
import br.dev.singular.overview.domain.usecase.suggestion.IGetAllSuggestionsUseCase
import br.dev.singular.overview.presentation.UiState
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.presentation.ui.utils.mappers.toUi
import br.dev.singular.overview.ui.model.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias SuggestionUIState = UiState<Map<String, List<MediaUiModel>>>

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val _repository: IMediaSearchPagingRepository,
    private val suggestionsUseCase: IGetAllSuggestionsUseCase
) : ViewModel() {

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _suggestionsUIState = MutableStateFlow<SuggestionUIState>(UiState.Loading())
    val suggestionsUIState: StateFlow<SuggestionUIState> = _suggestionsUIState

    var mediasSearch: Flow<PagingData<MediaUiModel>> = onLoadMediaSearching().toUi()
        private set

    fun onSearching(filters: SearchFilters) {
        _searchFilters.value = filters
        mediasSearch = onLoadMediaSearching().toUi()
    }

    private fun onLoadMediaSearching() =
        _repository.searchPaging(_searchFilters.value)
            .flow.cachedIn(viewModelScope)

    fun onLoadSuggestions() = viewModelScope.launch {
        suggestionsUseCase.invoke().toUi { it.toUi() }.let {
            _suggestionsUIState.value = it
        }
    }
}
