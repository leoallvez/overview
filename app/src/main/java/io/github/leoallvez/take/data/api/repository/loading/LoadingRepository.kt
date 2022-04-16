package io.github.leoallvez.take.data.api.repository.loading

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.di.AbListSetup
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.experiment.AbExperiment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadingRepository @Inject constructor(
    private val localDataSource: LoadingLocalDataSource,
    @AbListSetup
    val experiment: AbExperiment<List<Suggestion>>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun loadingSuggestion(): LiveData<Boolean> {
        return withContext(ioDispatcher) {
            //TODO: check cache time
            val suggestions = experiment.execute()

            suggestions.forEach { s ->
                val suggestionId = localDataSource.saveSuggestion(s)
                Log.i("suggestion_tag", "id: $suggestionId")
            }

            //TODO: load movies and tv show of API

            MutableLiveData(true)
        }
    }

    private suspend fun lab(action: () -> Unit) {
        experiment.execute().forEach { suggestion ->
            val suggestionId = localDataSource.saveSuggestion(suggestion)
        }

    }

}
