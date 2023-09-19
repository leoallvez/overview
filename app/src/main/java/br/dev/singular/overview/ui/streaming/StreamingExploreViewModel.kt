package br.dev.singular.overview.ui.streaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.repository.genre.IGenreRepository
import br.dev.singular.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.dev.singular.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import br.dev.singular.overview.IAnalyticsTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreamingExploreViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _genreRepository: IGenreRepository,
    private val _mediaRepository: IMediaPagingRepository
) : ViewModel() {

    private val _searchFilters = MutableStateFlow(SearchFilters())
    val searchFilters: StateFlow<SearchFilters> = _searchFilters

    private val _genres = MutableStateFlow<List<GenreEntity>>(listOf())
    val genres: StateFlow<List<GenreEntity>> = _genres

    fun getMediasPaging(streamingId: Long): Flow<PagingData<Media>> {
        _searchFilters.value.streamingsIds = listOf(streamingId)
        return _mediaRepository.getMediasPaging(searchFilters.value)
    }

    fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        val type = searchFilters.value.mediaType
        _genres.value = _genreRepository.getItemsByMediaType(type)
    }

    fun updateFilters(searchFilters: SearchFilters) {
        _searchFilters.value = searchFilters
    }
}
