package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.source.MediaResult
import io.github.leoallvez.take.data.source.mediaitem.MediaLocalDataSource
import io.github.leoallvez.take.data.source.mediaitem.MediaRemoteDataSource
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.util.toSuggestionResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    @IoDispatcher private val _ioDispatcher: CoroutineDispatcher,
    private val _localDataSource: MediaLocalDataSource,
    private val _remoteDataSource: MediaRemoteDataSource,
    private val _suggestionLocalDataSource: SuggestionLocalDataSource,
) {

    private val _suggestionsWithItems: Map<Suggestion, List<MediaItem>> by lazy {
        _suggestionLocalDataSource.getWithMediaItems()
    }

    suspend fun getData(): Flow<List<SuggestionResult>> {
        return withContext(_ioDispatcher) {
            val results = if(_suggestionsWithItems.isNotEmpty()) {
                getLocalData()
            } else {
                getRemoteData()
            }
            flow { emit(results) }
        }
    }

    private suspend fun getRemoteData(): List<SuggestionResult> {
        val result = mutableListOf<SuggestionResult>()
        getSuggestions().forEach { suggestion ->
            val response = doRequest(suggestion.apiPath)
            if(response is MediaResult.ApiSuccess) {
                val items = response.items
                setForeignKeyOnItems(items, suggestion.dbId)
                saveItems(items)
                val suggestionResult = suggestion.toSuggestionResult(items)
                result.add(suggestionResult)
            }
        }
        return result.sortedBy { it.order }
    }

    private fun getSuggestions(): List<Suggestion> {
        return _suggestionLocalDataSource.getAll()
    }

    private fun getLocalData(): List<SuggestionResult> {
        return _suggestionsWithItems
            .map { it.toSuggestionResult() }
    }

    private suspend fun doRequest(apiPath: String): MediaResult {
        return _remoteDataSource.get(apiPath)
    }

    private fun setForeignKeyOnItems(
        items: List<MediaItem>,
        suggestionId: Long
    ) {
        items.forEach {
            it.suggestionId = suggestionId
        }
    }

    private suspend fun saveItems(
        items: List<MediaItem>
    ) {
        _localDataSource.update(*items.toTypedArray())
    }
}
