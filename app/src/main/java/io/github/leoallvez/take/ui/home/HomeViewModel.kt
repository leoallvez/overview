package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.repository.AudioVisualItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val _experiment: AbTest<Boolean>,
    private val _repository: AudioVisualItemRepository,
) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _featured: MutableLiveData<List<AudioVisualItem>> = MutableLiveData()
    val featured: LiveData<List<AudioVisualItem>> = _featured

    fun getSuggestions(): LiveData<List<SuggestionResult>> = liveData {
        val result = sliceFeatured(suggestions = _repository.getData().first())
        emit(value = result)
    }

    private fun sliceFeatured(
        suggestions: List<SuggestionResult>
    ): MutableList<SuggestionResult> {

        val featured = suggestions.first()
        _featured.value = featured.items.take(5)
        val result = suggestions.toMutableList()
        result.remove(featured)
        return result
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = _experiment.execute())
    }
}
