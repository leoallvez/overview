package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.Audiovisual
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

abstract class AudioVisualRepository(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getData(): Flow<List<SuggestionResult>> {
        return withContext(ioDispatcher) {
            val hasCache = hasCache()
            val results = if (hasCache) getLocalData() else getRemoteData()
            flow { emit(results) }
        }
    }

    abstract fun hasCache(): Boolean

    abstract fun getLocalData(): List<SuggestionResult>

    private suspend fun getRemoteData(): List<SuggestionResult> {
        val results = mutableListOf<SuggestionResult>()
        val suggestions = getSuggestions()
        suggestions.forEach { suggestion ->
            val response = doRequest(suggestion.apiPath)
            if(response is AudiovisualResult.ApiSuccess) {
                val audiovisual = response.content
                saveCache(audiovisual, suggestion.suggestionId)
                val suggestionResult = suggestion.toSuggestionResult(audiovisual)
                results.add(suggestionResult)
            }
        }
        return results
    }

    abstract suspend fun doRequest(apiPath: String): AudiovisualResult

    abstract fun getSuggestions(): List<Suggestion>

    abstract suspend fun saveCache(
        audiovisuals: List<Audiovisual>,
        suggestionId: Long
    )
}