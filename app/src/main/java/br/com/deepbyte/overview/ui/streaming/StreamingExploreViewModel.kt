package br.com.deepbyte.overview.ui.streaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.data.repository.mediatype.IMediaTypeRepository
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StreamingExploreViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _mediaRepository: IMediaPagingRepository,
    private val _mediaTypeRepository: IMediaTypeRepository
) : ViewModel() {

    private val _genres = MutableStateFlow<List<Genre>>(listOf())
    val genres: StateFlow<List<Genre>> = _genres

    fun getMediasPaging(mediaType: MediaTypeEnum, streamingApiId: Long) =
        _mediaRepository.getMediasPaging(mediaType, listOf(streamingApiId))

    fun loadGenresByMediaType(mediaType: MediaTypeEnum) =
        viewModelScope.launch(Dispatchers.IO) {
            _genres.value = _mediaTypeRepository.getItemWithGenres(mediaType)
            Timber.i("genres size ${_genres.value.size}")
        }
}
