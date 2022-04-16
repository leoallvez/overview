package io.github.leoallvez.take.data.repository.audiovisual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudiovisualRepository @Inject constructor(
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun refresh() {
        return withContext(ioDispatcher) {
            val suggestion = suggestionLocalDataSource.getAll()


            //TODO: load movies and tv show of API

        }
    }

}
