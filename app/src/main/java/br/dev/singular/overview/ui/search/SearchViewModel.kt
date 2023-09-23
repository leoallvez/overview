package br.dev.singular.overview.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.repository.search.ISearchPagingRepository
import br.dev.singular.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: ISearchPagingRepository
) : ViewModel() {

    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters

    var medias: Flow<PagingData<Media>> = _repository.searchPaging(_filters.value)
        private set

    fun updateData(filters: SearchFilters) {
        updateFilters(filters)
        reloadMedias()
    }

    private fun reloadMedias() {
        medias = _repository.searchPaging(_filters.value)
    }

    private fun updateFilters(filters: SearchFilters) = with(filters) {
        _filters.value = SearchFilters(query, mediaType)
    }
}
