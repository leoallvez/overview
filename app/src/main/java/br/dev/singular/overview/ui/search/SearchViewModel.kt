package br.dev.singular.overview.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaSearchPagingRepository
import br.dev.singular.overview.domain.usecase.suggestion.IGetAllSuggestionsUseCase
import br.dev.singular.overview.presentation.UIState
import br.dev.singular.overview.presentation.model.MediaUIModel
import br.dev.singular.overview.presentation.model.toUIMap
import br.dev.singular.overview.presentation.ui.utils.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias SuggestionUIState = UIState<Map<String, List<MediaUIModel>>>

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val _repository: IMediaSearchPagingRepository,
    private val suggestionsUseCase: IGetAllSuggestionsUseCase
) : ViewModel() {

    init {
        onLoadSuggestions()
    }

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _suggestionsUIState = MutableStateFlow<SuggestionUIState>(UIState.Loading())
    val suggestionsUIState: StateFlow<SuggestionUIState> = _suggestionsUIState

    var mediasSearch: Flow<PagingData<MediaEntity>> = onLoadMediaSearching()
        private set

    fun onSearching(filters: SearchFilters) {
        _searchFilters.value = filters
        mediasSearch = onLoadMediaSearching()
    }

    private fun onLoadMediaSearching() =
        _repository.searchPaging(_searchFilters.value)
            .flow.cachedIn(viewModelScope)

    private fun onLoadSuggestions() = viewModelScope.launch {
        suggestionsUseCase.invoke().toUiState { it.toUIMap() }.let {
            _suggestionsUIState.value = it
        }
    }
}
