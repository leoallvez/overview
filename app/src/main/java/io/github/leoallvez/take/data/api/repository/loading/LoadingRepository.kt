package io.github.leoallvez.take.data.api.repository.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.api.repository.discovery.DiscoveryDataSource
import io.github.leoallvez.take.data.model.Suggestions
import io.github.leoallvez.take.di.AbListSetup
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.experiment.AbExperiment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadingRepository @Inject constructor(
    private val dataSource: DiscoveryDataSource,
    @AbListSetup
    val experiment: AbExperiment<List<Suggestions>>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun loadingSuggestion(): LiveData<Boolean> {
        return withContext(ioDispatcher) {

            val suggestions = experiment.execute()
            suggestions.forEach { s ->
                //TODO: load on api;
            }

            MutableLiveData(true)
        }
    }



}