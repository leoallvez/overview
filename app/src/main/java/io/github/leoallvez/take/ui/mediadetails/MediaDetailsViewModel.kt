package io.github.leoallvez.take.ui.mediadetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.repository.MediaDetailsRepository
import io.github.leoallvez.take.data.source.ApiResult
import io.github.leoallvez.take.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val _repository: MediaDetailsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun getMediaDetails(id: Long, type: String) = viewModelScope.launch {
        _repository.getMediaDetailsResult(id, type).collect { result ->
            val isSuccess = result is ApiResult.Success
            _uiState.value = if (isSuccess) UiState.Success(data = result.data) else UiState.Error
        }
    }
}
