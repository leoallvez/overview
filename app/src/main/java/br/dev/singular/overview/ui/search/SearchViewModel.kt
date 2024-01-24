package br.dev.singular.overview.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaSearchPagingRepository
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
    private val _repository: IMediaSearchPagingRepository
) : ViewModel() {

    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters

    var medias: Flow<PagingData<MediaEntity>> = loadMediaPaging()
        private set

    fun updateFilter(filters: SearchFilters) {
        _filters.value = filters
        medias = loadMediaPaging()
    }

    private fun loadMediaPaging() =
        _repository.searchPaging(_filters.value).flow.cachedIn(viewModelScope)
}
