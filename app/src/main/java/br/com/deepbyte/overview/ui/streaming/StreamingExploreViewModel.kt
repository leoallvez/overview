package br.com.deepbyte.overview.ui.streaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.model.Filters
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.repository.genre.IGenreRepository
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.di.ShowAds
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
    val analyticsTracker: IAnalyticsTracker,
    private val _genreRepository: IGenreRepository,
    private val _mediaRepository: IMediaPagingRepository
) : ViewModel() {

    private val _filters = MutableStateFlow(Filters())
    val filters: StateFlow<Filters> = _filters

    private val _genres = MutableStateFlow<List<Genre>>(listOf())
    val genres: StateFlow<List<Genre>> = _genres

    fun getMediasPaging(streamingId: Long): Flow<PagingData<Media>> {
        filters.value.streamingsIds = listOf(streamingId)
        return _mediaRepository.getMediasPaging(filters.value)
    }

    fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        val type = filters.value.mediaType
        _genres.value = _genreRepository.getItemsByMediaType(type)
    }

    fun updateFilters(filters: Filters) {
        _filters.value = filters
    }
}