package br.dev.singular.overview.ui.streaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.dev.singular.overview.data.source.CacheDataSource
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.util.fromJson
import br.dev.singular.overview.util.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreamingExploreViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    private val _cache: CacheDataSource,
    val analyticsTracker: IAnalyticsTracker,
    private val _genreRepository: IGenreRepository,
    private val _mediaRepository: IMediaPagingRepository
) : ViewModel() {

    private var _cacheNotLoaded: Boolean = true

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _genres = MutableStateFlow<List<GenreEntity>>(listOf())
    val genres: StateFlow<List<GenreEntity>> = _genres

    var medias: Flow<PagingData<Media>> = _mediaRepository.getMediasPaging(searchFilters.value)
        private set

    init {
        loadFilterCache()
    }

    fun setStreamingId(streamingId: Long) {
        _searchFilters.value.streamingId = streamingId
    }

    fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        _genres.value = _genreRepository.getItemsByMediaType(searchFilters.value.mediaType)
    }

    fun updateData(filters: SearchFilters) {
        updateFilters(filters)
        reloadMedias()
        setFilterCache()
    }

    fun reloadMedias() {
        medias = _mediaRepository.getMediasPaging(searchFilters.value)
    }

    private fun updateFilters(filters: SearchFilters) = with(filters) {
        _searchFilters.value = SearchFilters(
            mediaType = mediaType,
            genresIds = genresIds,
            streamingId = streamingId
        )
    }

    private fun loadFilterCache() = viewModelScope.launch(Dispatchers.IO) {
        _cache.getValue(CacheDataSource.KEY_FILTER_CACHE).collect { jsonFiltersCache ->
            if (_cacheNotLoaded && jsonFiltersCache != null) {
                val filters = jsonFiltersCache.fromJson<SearchFilters>()
                filters?.let {
                    _searchFilters.value = filters
                    _cacheNotLoaded = false
                    reloadMedias()
                }
            }
        }
    }

    private fun setFilterCache() = viewModelScope.launch(Dispatchers.IO) {
        _cache.setValue(CacheDataSource.KEY_FILTER_CACHE, searchFilters.value.toJson())
    }
}
