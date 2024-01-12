package br.dev.singular.overview.ui.streaming.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.model.provider.StreamingEntity
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaPagingRepository
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import br.dev.singular.overview.data.source.CacheDataSource
import br.dev.singular.overview.data.source.CacheDataSource.Companion.KEY_FILTER_CACHE
import br.dev.singular.overview.di.IoDispatcher
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.util.fromJson
import br.dev.singular.overview.util.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreStreamingViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    private val _cache: CacheDataSource,
    val analyticsTracker: IAnalyticsTracker,
    private val _genreRepository: IGenreRepository,
    private val _mediaRepository: IMediaPagingRepository,
    private val _streamingRepository: ISelectedStreamingRepository,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        loadFilterCache()
        loadSelectedStreaming()
    }

    private var _filterCacheNotLoaded: Boolean = true

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _genres = MutableStateFlow<List<GenreEntity>>(listOf())
    val genres: StateFlow<List<GenreEntity>> = _genres

    var medias: Flow<PagingData<Media>> = emptyFlow()
        private set

    var selectedStreaming: StreamingEntity? = null
        private set

    fun updateData(filters: SearchFilters) {
        updateFilters(filters)
        loadMedias()
        setFilterCache()
    }

    private fun updateFilters(filters: SearchFilters) = with(filters) {
        _searchFilters.value = _searchFilters.value.copy(
            genreId = genreId,
            mediaType = mediaType,
            streamingId = selectedStreaming?.apiId
        )
    }

    fun loadMedias() {
        medias = _mediaRepository.getPaging(searchFilters.value)
    }

    fun loadGenres() = viewModelScope.launch(_dispatcher) {
        _genres.value = _genreRepository.getItemsByMediaType(searchFilters.value.mediaType)
    }

    private fun loadFilterCache() = viewModelScope.launch(_dispatcher) {
        _cache.getValue(KEY_FILTER_CACHE).collect { jsonFiltersCache ->
            if (_filterCacheNotLoaded && jsonFiltersCache != null) {
                val filters = jsonFiltersCache.fromJson<SearchFilters>()
                filters?.let {
                    _searchFilters.value = filters
                    _filterCacheNotLoaded = false
                }
            }
        }
    }

    private fun loadSelectedStreaming() = viewModelScope.launch(_dispatcher) {
        _streamingRepository.getSelectedItem().collect { streaming ->
            Timber.i("loadSelectedStreaming: $streaming")
            selectedStreaming = streaming
            _searchFilters.value = _searchFilters.value.copy(streamingId = streaming?.apiId)
            delay(500)
            loadMedias()
        }
    }

    private fun setFilterCache() = viewModelScope.launch(_dispatcher) {
        _cache.setValue(KEY_FILTER_CACHE, searchFilters.value.toJson())
    }
}
