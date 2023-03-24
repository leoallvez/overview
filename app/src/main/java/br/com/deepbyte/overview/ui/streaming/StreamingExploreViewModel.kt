package br.com.deepbyte.overview.ui.streaming

import androidx.lifecycle.ViewModel
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaPagingRepository
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StreamingExploreViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IMediaPagingRepository
) : ViewModel() {

    fun getMediasPaging(mediaType: MediaType, streamingApiId: Long) =
        _repository.getMediasPaging(mediaType, listOf(streamingApiId))
}
