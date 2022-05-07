package io.github.leoallvez.take.ui.home

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.AudioVisualManager
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.experiment.AbExperiment
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val experiment: AbExperiment<Boolean>,
    private val audioVisualManager: AudioVisualManager
) : ViewModel() {

    private var isNotLoaded = true

    fun getSuggestions(): LiveData<List<SuggestionResult>> {
        if(isNotLoaded) {
            isNotLoaded = false
            return audioVisualManager.getData().asLiveData()
        }
        return MutableLiveData(listOf())
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = experiment.execute())
    }
}
