package br.com.deepbyte.overview.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.data.repository.search.ISearchRepository
import br.com.deepbyte.overview.di.ShowAds
import br.com.deepbyte.overview.ui.SearchUiState
import br.com.deepbyte.overview.ui.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    private val _repository: ISearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchState.NotStated())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun search(query: String) = viewModelScope.launch {
        if (query.isNotEmpty()) {
            searchResults(query)
        } else {
            _uiState.value = SearchState.Empty()
        }
    }

    private suspend fun searchResults(query: String) {
        _uiState.value = SearchState.Loading()
        _repository.search(query).collect { result ->
            _uiState.value = if (result.isNotEmpty) SearchState.Success(result) else SearchState.Empty()
        }
    }
}
