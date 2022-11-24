package br.com.deepbyte.overview.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.overview.IAnalyticsTracker
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.data.repository.MediaSuggestionManager
import br.com.deepbyte.overview.di.MainDispatcher
import br.com.deepbyte.overview.di.ShowAds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ShowAds val showAds: Boolean,
    val analyticsTracker: IAnalyticsTracker,
    private val _manager: MediaSuggestionManager,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    val featuredMediaItems: LiveData<List<MediaItem>> = _manager.featuredMediaItems

    val suggestions: LiveData<List<MediaSuggestion>> = _manager.mediaSuggestions

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(_mainDispatcher) {
            _loading.value = true
            _manager.refresh()
            _loading.value = false
        }
    }

//    fun adsAreVisible(): LiveData<Boolean> = liveData {
//        emit(value = _experiment.execute())
//    }
}
