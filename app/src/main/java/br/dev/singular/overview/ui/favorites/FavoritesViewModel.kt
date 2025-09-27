package br.dev.singular.overview.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityPagingRepository
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.presentation.model.MediaUiModel
import br.dev.singular.overview.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val _repository: IMediaEntityPagingRepository
) : ViewModel() {

    private val _mediaType = MutableStateFlow(MediaType.ALL)
    val mediaType: StateFlow<MediaType> = _mediaType

    var medias:  Flow<PagingData<MediaUiModel>> = loadMediaPaging()
        private set

    fun updateType(type: MediaType) {
        _mediaType.value = type
        medias = loadMediaPaging()
    }

    private fun loadMediaPaging() =
        _repository.getLikedPaging(mediaType.value)
            .flow.cachedIn(viewModelScope)
            .toUiModel()

}
