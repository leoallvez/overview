package br.com.deepbyte.overview.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.data.model.provider.Streaming
import br.com.deepbyte.overview.data.repository.MediaSuggestionManager
import br.com.deepbyte.overview.data.repository.streaming.IStreamingRepository
import br.com.deepbyte.overview.di.MainDispatcher
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _manager: MediaSuggestionManager,
    private val _repository: IStreamingRepository,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        refresh()
        loadStreaming()
    }

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    val featuredMediaItems: LiveData<List<MediaItem>> = _manager.featuredMediaItems

    val suggestions: LiveData<List<MediaSuggestion>> = _manager.mediaSuggestions

    private val _streaming = MutableStateFlow<List<Streaming>>(listOf())
    val streaming: StateFlow<List<Streaming>> = _streaming

    fun refresh() {
        viewModelScope.launch(_mainDispatcher) {
            _loading.value = true
            _manager.refresh()
            _loading.value = false
        }
    }

    private fun loadStreaming() {
        viewModelScope.launch(_mainDispatcher) {
            _repository.getAllSelected().collect { streamings ->
                _streaming.value = streamings
            }
        }
    }
}
