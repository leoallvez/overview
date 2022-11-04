package io.github.leoallvez.take.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.DiscoverRepository
import io.github.leoallvez.take.ui.DiscoverUiState
import io.github.leoallvez.take.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val _repository: DiscoverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DiscoverUiState>(UiState.Loading())
    val uiState: StateFlow<DiscoverUiState> = _uiState

    fun loadDada(providerId: Long, mediaType: String) {
        _uiState.value = UiState.Success(
            data = _repository.discoverOnTvByProvider(providerId, mediaType, viewModelScope)
        )
    }
}
