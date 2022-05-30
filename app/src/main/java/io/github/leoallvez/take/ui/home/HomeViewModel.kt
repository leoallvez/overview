package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.repository.AudioVisualItemRepository
import io.github.leoallvez.take.data.repository.SuggestionRepository
import io.github.leoallvez.take.di.AbDisplayAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val _experiment: AbTest<Boolean>,
    private val _suggestionRepository: SuggestionRepository,
    private val _audioVisualItemRepository: AudioVisualItemRepository,
) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _featured: MutableLiveData<List<AudioVisualItem>> = MutableLiveData()
    val featured: LiveData<List<AudioVisualItem>> = _featured

    private val _suggestions: MutableLiveData<List<SuggestionResult>> = MutableLiveData()
    val suggestions: LiveData<List<SuggestionResult>> = _suggestions

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.Main) {
            _loading.value = true
            _suggestionRepository.refresh()
            setData()
            _loading.value = false
        }
    }

    private suspend fun setData() {
        val suggestions = _audioVisualItemRepository.getData().first()
        _suggestions.value = if(suggestions.isNotEmpty()) {
            val featured = suggestions.first()
            _featured.value = featured.items.take(10)
            val result = suggestions.toMutableList()
            result.remove(featured)
            result
        } else {
            listOf()
        }
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = _experiment.execute())
    }
}
