package br.dev.singular.overview.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.data.local.source.CacheDataSource
import br.dev.singular.overview.data.local.source.CacheDataSource.Companion.KEY_FILTER_CACHE
import br.dev.singular.overview.data.local.source.CacheDataSource.Companion.KEY_SHOW_HIGHLIGHT_STREAMING_ICON
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.media.remote.interfaces.IMediaPagingRepository
import br.dev.singular.overview.data.repository.streaming.selected.ISelectedStreamingRepository
import br.dev.singular.overview.di.HighlightIconsQualifier
import br.dev.singular.overview.di.IoDispatcher
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.remote.RemoteConfig
import br.dev.singular.overview.ui.model.toUiModel
import br.dev.singular.overview.util.fromJson
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _cache: CacheDataSource,
    private val _genreRepository: IGenreRepository,
    private val _mediaRepository: IMediaPagingRepository,
    @HighlightIconsQualifier
    private val highlightIconsManager: RemoteConfig<Boolean>,
    private val _streamingRepository: ISelectedStreamingRepository,
    @IoDispatcher private val _dispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        viewModelScope.launch(_dispatcher) {
            delay(timeMillis = 500)
            prepareData()
        }
    }

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _genres = MutableStateFlow<List<GenreEntity>>(listOf())
    val genres: StateFlow<List<GenreEntity>> = _genres

    var medias: Flow<PagingData<MediaUiModel>> = emptyFlow()
        private set

    private val _showHighlightIcon = MutableStateFlow(true)
    val showHighlightIcon: StateFlow<Boolean> = _showHighlightIcon

    fun updateData(filters: SearchFilters) = viewModelScope.launch(_dispatcher) {
        _searchFilters.value = filters
        loadMediaPaging()
        loadGenres()
        setFilter()
    }

    private suspend fun prepareData() {
        loadFilter()
        loadStreaming()
        loadMediaPaging()
        loadGenres()
        setFilter()
        loadHighlightIcons()
    }

    fun loadMediaPaging() {
        medias = _mediaRepository
            .getPaging(searchFilters.value).flow.cachedIn(viewModelScope).toUiModel()
    }

    private suspend fun loadGenres() {
        _genres.value = _genreRepository.getItemsByMediaType(searchFilters.value.mediaType)
            .sortedBy { it.name }
    }

    private suspend fun loadFilter() {
        val json = _cache.getValue(KEY_FILTER_CACHE).first()
        val filters = json.fromJson<SearchFilters>()
        filters?.let { _searchFilters.value = filters }
    }

    private suspend fun loadStreaming() {
        val streaming = _streamingRepository.getSelectedItem().first()
        streaming?.let {
            _searchFilters.value = _searchFilters.value.copy(streaming = streaming)
        }
    }

    private suspend fun setFilter() {
        _cache.setValue(KEY_FILTER_CACHE, searchFilters.value.toJson())
    }

    private suspend fun loadHighlightIcons() {
        val hasPermissionToHighlightIcon = highlightIconsManager.execute()
        _showHighlightIcon.value = hasPermissionToHighlightIcon && shouldShowHighlightIcon()
    }

    private suspend fun shouldShowHighlightIcon(): Boolean {
        val result = _cache.getValue(KEY_SHOW_HIGHLIGHT_STREAMING_ICON).first()
        return (result ?: true)
    }
}
