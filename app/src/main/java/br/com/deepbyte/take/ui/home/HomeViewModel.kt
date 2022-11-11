package br.com.deepbyte.take.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import br.com.deepbyte.take.abtest.AbTest
import br.com.deepbyte.take.data.model.MediaItem
import br.com.deepbyte.take.data.model.MediaSuggestion
import br.com.deepbyte.take.data.repository.MediaSuggestionManager
import br.com.deepbyte.take.di.AbDisplayAds
import br.com.deepbyte.take.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _manager: MediaSuggestionManager,
    @AbDisplayAds private val _experiment: AbTest<Boolean>,
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

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = _experiment.execute())
    }
}
