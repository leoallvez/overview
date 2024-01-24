package br.dev.singular.overview.ui.liked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.IAnalyticsTracker
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityPagingRepository
import br.dev.singular.overview.data.source.media.MediaType
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

    private val _mediaType = MutableStateFlow(MediaType.ALL)
    val mediaType: StateFlow<MediaType> = _mediaType

    var medias: Flow<PagingData<MediaEntity>> = loadMediaPaging()
        private set

    fun updateType(type: MediaType) {
        _mediaType.value = type
        medias = loadMediaPaging()
    }

    private fun loadMediaPaging() =
        _repository.getLikedPaging(mediaType.value).flow.cachedIn(viewModelScope)
}
