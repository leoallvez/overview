package io.github.leoallvez.take.ui.mediadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.repository.MediaDetailsRepository
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private typealias MediaUiState = UiState<MediaDetailResponse?>

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val _repository: MediaDetailsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaUiState>(UiState.Loading())
    val uiState: StateFlow<MediaUiState> = _uiState

    fun loadMediaDetails(apiId: Long, mediaType: String) = viewModelScope.launch {
        _repository.getMediaDetailsResult(apiId, mediaType).collect { result ->
            val isSuccess = result is DataResult.Success
            _uiState.value = if (isSuccess) UiState.Success(result.data) else UiState.Error()
        }
    }

    fun refresh(apiId: Long, mediaType: String) {
        _uiState.value = UiState.Loading()
        loadMediaDetails(apiId, mediaType)
    }
}
