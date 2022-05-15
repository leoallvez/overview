package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.AudioVisualManager
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.abtest.AbTest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val experiment: AbTest<Boolean>,
    private val audioVisualManager: AudioVisualManager
) : ViewModel() {

    fun getSuggestions(): LiveData<List<SuggestionResult>> =
        audioVisualManager.getData().asLiveData()

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = experiment.execute())
    }
}
