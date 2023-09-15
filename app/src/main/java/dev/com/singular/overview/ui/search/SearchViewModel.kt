package dev.com.singular.overview.ui.search

import androidx.lifecycle.ViewModel
import dev.com.singular.overview.IAnalyticsTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.repository.search.ISearchPagingRepository
import dev.com.singular.overview.di.ShowAds
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
