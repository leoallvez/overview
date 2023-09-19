package br.dev.singular.overview.ui.search

import androidx.lifecycle.ViewModel
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.repository.search.ISearchPagingRepository
import br.dev.singular.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: ISearchPagingRepository
) : ViewModel() {

    var started: Boolean = false
        private set

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    fun searchPaging() = _repository.searchPaging(_searchFilters.value)

    fun start() {
        started = true
    }
}
