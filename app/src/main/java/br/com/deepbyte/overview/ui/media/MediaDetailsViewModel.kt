package br.com.deepbyte.overview.ui.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.abtesting.AbTesting
import br.com.deepbyte.overview.data.repository.MediaDetailsRepository
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.di.AbDisplayAds
import br.com.deepbyte.overview.ui.MediaUiState
import br.com.deepbyte.overview.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    val analyticsTracker: IAnalyticsTracker,
    @AbDisplayAds private val _experiment: AbTesting<Boolean>,
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

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = _experiment.execute())
    }
}
