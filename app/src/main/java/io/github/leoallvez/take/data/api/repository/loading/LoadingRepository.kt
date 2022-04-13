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
    private val dataSource: LoadingDataSource,
    @AbListSetup
    val experiment: AbExperiment<List<Suggestions>>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun loadingSuggestion(): LiveData<Boolean> {
        return withContext(ioDispatcher) {

            val suggestions = experiment.execute()

            //TODO: load movies and tv show of API

            dataSource.refreshSuggestions(suggestions)

            MutableLiveData(true)
        }
    }

    /**
    sealed class RequestSongResult {
    object Loading : RequestSongResult()
    object Success : RequestSongResult()
    class ApiError(val code: Int, val message: String?) : RequestSongResult()
    class UnknownError(val message: String?) : RequestSongResult()
    }
     */




}