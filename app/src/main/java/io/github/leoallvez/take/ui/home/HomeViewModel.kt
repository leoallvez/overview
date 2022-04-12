package io.github.leoallvez.take.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leoallvez.take.data.api.repository.discovery.DiscoveryRepository
import io.github.leoallvez.take.di.AbDisplayAds
import io.github.leoallvez.take.experiment.AbExperiment
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @AbDisplayAds
    private val experiment: AbExperiment<Boolean>,
    private val repository: DiscoveryRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            val setupList = repository.getListsSetup()
            val result = setupList
        }
    }

    fun adsAreVisible(): LiveData<Boolean> = liveData {
        emit(value = experiment.execute())
    }

}
