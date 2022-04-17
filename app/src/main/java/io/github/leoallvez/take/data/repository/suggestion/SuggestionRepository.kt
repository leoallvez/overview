package io.github.leoallvez.take.data.repository.suggestion

import android.util.Log
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.data.source.suggestion.SuggestionRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SuggestionRepository @Inject constructor(
    private val localDataSource: SuggestionLocalDataSource,
    private val remoteDataSource: SuggestionRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun refresh() = withContext (ioDispatcher) {
        //TODO: Write cache logic
        val suggestions = remoteDataSource.get().toTypedArray()
        localDataSource.deleteAll()
        localDataSource.save(*suggestions)
    }

}