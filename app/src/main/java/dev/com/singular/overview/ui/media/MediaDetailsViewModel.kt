package dev.com.singular.overview.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.com.singular.overview.IAnalyticsTracker
import dev.com.singular.overview.data.source.media.MediaTypeEnum
import dev.com.singular.overview.ui.MediaUiState
import dev.com.singular.overview.ui.UiState
import dev.com.singular.overview.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.com.singular.overview.data.repository.media.interfaces.IMediaRepository
import dev.com.singular.overview.di.ShowAds
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

    private val _dataNotLoaded
        get() = (_uiState.value is UiState.Success).not()

    fun loadMediaDetails(apiId: Long, type: MediaTypeEnum) = viewModelScope.launch {
        if (_dataNotLoaded) {
            _repository.getItem(apiId, type).collect { result ->
                _uiState.value = result.toUiState()
            }
        }
    }

    fun refresh(apiId: Long, type: MediaTypeEnum) {
        _uiState.value = UiState.Loading()
        loadMediaDetails(apiId, type)
    }
}
