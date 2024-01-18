package br.dev.singular.overview.ui.streaming.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaPagingRepository
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import br.dev.singular.overview.data.source.CacheDataSource
import br.dev.singular.overview.data.source.CacheDataSource.Companion.KEY_FILTER_CACHE
import br.dev.singular.overview.di.IoDispatcher
import br.dev.singular.overview.di.ShowAds
import br.dev.singular.overview.util.nullableFromJson
import br.dev.singular.overview.util.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
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

    private var filterLoaded: Boolean = false

    init {
        viewModelScope.launch(_dispatcher) {
            delay(timeMillis = 500)
            loadData()
        }
    }

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _genres = MutableStateFlow<List<GenreEntity>>(listOf())
    val genres: StateFlow<List<GenreEntity>> = _genres

    var medias: Flow<PagingData<MediaEntity>> = emptyFlow()
        private set

    fun updateData(filters: SearchFilters) {
        _searchFilters.value = filters
        loadMediaPaging()
        viewModelScope.launch(_dispatcher) { setFilter() }
    }

    private suspend fun loadData() {
        Timber.tag("steaming_values").d(message = "START")
        loadFilter()
        loadStreaming()
        loadMediaPaging()
        setFilter()
        Timber.tag("steaming_values").d(message = "END")
    }

    fun loadMediaPaging() {
        Timber.tag("steaming_values").d(message = "load_media")
        medias = _mediaRepository.getPaging(searchFilters.value).flow.cachedIn(viewModelScope)
    }

    fun loadGenres() = viewModelScope.launch(_dispatcher) {
        Timber.tag("steaming_values").d(message = "load_genre")
        _genres.value = _genreRepository.getItemsByMediaType(searchFilters.value.mediaType)
    }

    private suspend fun loadFilter() {
        val jsonFilters = _cache.getValue(KEY_FILTER_CACHE).first()
        jsonFilters.nullableFromJson<SearchFilters>()?.apply {
            Timber.tag("steaming_values").d(message = "load_f: $streaming")
            _searchFilters.value = this
        }
    }

    private suspend fun loadStreaming() {
        val streaming = _streamingRepository.getSelectedItem().first()
        Timber.tag("steaming_values").d(message = "load_s: $streaming")
        _searchFilters.value = _searchFilters.value.copy(streaming = streaming)
    }

    private suspend fun setFilter() {
        Timber.tag("steaming_values").d(message = "load_x: ${searchFilters.value.streaming}")
        _cache.setValue(KEY_FILTER_CACHE, searchFilters.value.toJson())
    }
}
