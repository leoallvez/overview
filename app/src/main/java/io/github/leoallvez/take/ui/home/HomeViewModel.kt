package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.abtest.AbTest
import io.github.leoallvez.take.data.repository.AudioVisualItemRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val experiment: AbTest<Boolean>,
    private val audioVisualItemRepository: AudioVisualItemRepository,
) : ViewModel() {

    fun getSuggestions(): LiveData<List<SuggestionResult>> = liveData {
        val result = audioVisualItemRepository.getData()
        emit(value = result.first())
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = experiment.execute())
    }
}
