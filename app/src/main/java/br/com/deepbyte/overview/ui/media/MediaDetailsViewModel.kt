package br.com.deepbyte.overview.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.repository.media.interfaces.IMediaRepository
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.di.ShowAds
import br.com.deepbyte.overview.ui.MediaUiState
import br.com.deepbyte.overview.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _repository: IMediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaUiState>(UiState.Loading())
    val uiState: StateFlow<MediaUiState> = _uiState

    fun loadMediaDetails(apiId: Long, type: MediaTypeEnum) = viewModelScope.launch {
        _repository.getItem(apiId, type).collect { result ->
            val isSuccess = result is DataResult.Success
            _uiState.value = if (isSuccess) UiState.Success(result.data) else UiState.Error()
        }
    }

    fun refresh(apiId: Long, type: MediaTypeEnum) {
        _uiState.value = UiState.Loading()
        loadMediaDetails(apiId, type)
    }
}
