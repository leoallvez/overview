package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.MediaSuggestion
import io.github.leoallvez.take.data.repository.ContentsManager
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val _manager: ContentsManager,
    @AbDisplayAds private val _experiment: AbTest<Boolean>,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    val featured: LiveData<List<MediaItem>> = _manager.featured

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
