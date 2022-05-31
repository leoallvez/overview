package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.repository.AudioVisualItemRepository
import io.github.leoallvez.take.data.repository.SuggestionRepository
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds private val _experiment: AbTest<Boolean>,
    private val _suggestionRepository: SuggestionRepository,
    private val _audioVisualRepository: AudioVisualItemRepository,
    @MainDispatcher private val _mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _featured = MutableLiveData<List<AudioVisualItem>>()
    val featured: LiveData<List<AudioVisualItem>> = _featured

    private val _suggestions = MutableLiveData<List<SuggestionResult>>()
    val suggestions: LiveData<List<SuggestionResult>> = _suggestions

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(_mainDispatcher) {
            _loading.value = true
            _suggestionRepository.refresh()
            setAttributes()
            _loading.value = false
        }
    }

    private suspend fun setAttributes() {
        val suggestions = getSuggestions()
        val result = if(suggestions.isNotEmpty()) {
            _featured.value = sliceFeatured(suggestions)
            suggestions
        } else {
            listOf()
        }
        _suggestions.value = result
    }

    private suspend fun getSuggestions(): MutableList<SuggestionResult> {
        return _audioVisualRepository
            .getData()
            .first()
            .toMutableList()
    }

    private fun sliceFeatured(
        suggestions: MutableList<SuggestionResult>
    ): List<AudioVisualItem> {
        val featured = suggestions.first()
        suggestions.remove(featured)
        return featured.items.take(MAXIMUM_OF_FEATURED)
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = _experiment.execute())
    }

    companion object {
        private const val MAXIMUM_OF_FEATURED = 10
    }
}
