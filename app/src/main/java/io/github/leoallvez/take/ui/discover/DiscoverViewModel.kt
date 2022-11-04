package io.github.leoallvez.take.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.repository.DiscoverRepository
import io.github.leoallvez.take.ui.PagingUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val _repository: DiscoverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PagingUiState>(PagingUiState.Loading)
    val uiState: StateFlow<PagingUiState> = _uiState

    var dataResult: Flow<PagingData<MediaItem>> = flow {}
        private set

    fun loadDada(providerId: Long, mediaType: String) {
        _uiState.value = PagingUiState.Success
        dataResult = _repository.discoverOnTvByProvider(providerId, mediaType ,viewModelScope)
    }
}
