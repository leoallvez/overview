package io.github.leoallvez.take.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.experiment.AbExperiment
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val experiment: AbExperiment<Boolean>
) : ViewModel() {

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = experiment.execute())
    }

}
