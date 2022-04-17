package io.github.leoallvez.take.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.repository.movie.MovieRepository
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.experiment.AbExperiment
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val experiment: AbExperiment<Boolean>,
    private val repository: MovieRepository
) : ViewModel() {

    fun getSuggestions(): LiveData<List<SuggestionResult>> = liveData {
        emitSource(repository.getData().asLiveData())
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = experiment.execute())
    }
}

