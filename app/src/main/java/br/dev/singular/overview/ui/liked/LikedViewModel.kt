package br.dev.singular.overview.ui.liked

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityPagingRepository
import br.dev.singular.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LikedViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IMediaEntityPagingRepository
) : ViewModel() {

    private val _filters = MutableStateFlow(SearchFilters())
    val filters: StateFlow<SearchFilters> = _filters

    var medias: Flow<PagingData<MediaEntity>> = _repository.getLikedPaging(filters.value).flow
        private set

    fun updateData(filters: SearchFilters) {
        updateFilters(filters)
        reloadMedias()
    }

    private fun reloadMedias() {
        medias = _repository.getLikedPaging(filters.value).flow
    }

    private fun updateFilters(filters: SearchFilters) {
        _filters.value = filters
    }
}
