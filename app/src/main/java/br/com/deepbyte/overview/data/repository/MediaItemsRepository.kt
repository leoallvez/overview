package br.com.deepbyte.overview.data.repository

import br.com.deepbyte.overview.data.api.response.ListContentResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.model.MediaSuggestion
import br.com.deepbyte.overview.data.model.Suggestion
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.media.IMediaRemoteDataSource
import br.com.deepbyte.overview.data.source.media.MediaLocalDataSource
import br.com.deepbyte.overview.data.source.suggestion.SuggestionLocalDataSource
import br.com.deepbyte.overview.di.IoDispatcher
import br.com.deepbyte.overview.util.toMediaSuggestion
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaItemsRepository @Inject constructor(
    @IoDispatcher private val _ioDispatcher: CoroutineDispatcher,
    private val _localDataSource: MediaLocalDataSource,
    private val _remoteDataSource: IMediaRemoteDataSource,
    private val _suggestionLocalDataSource: SuggestionLocalDataSource
) {

    private val _suggestionsWithItems: Map<Suggestion, List<MediaItem>> by lazy {
        _suggestionLocalDataSource.getWithMediaItems()
    }

    suspend fun getData(): Flow<List<MediaSuggestion>> {
        return withContext(_ioDispatcher) {
            val results = if (_suggestionsWithItems.isNotEmpty()) {
                getLocalData()
            } else {
                getRemoteData()
            }
            flow { emit(results) }
        }
    }

    private suspend fun getRemoteData(): List<MediaSuggestion> {
        val result = mutableListOf<MediaSuggestion>()
        getSuggestions().forEach { suggestion ->
            val response = doRequest(suggestion.apiPath)
            if (response is DataResult.Success) {
                response.data?.results?.let { items ->
                    setForeignKeyOnItems(items, suggestion.dbId)
                    saveItems(items)
                    val mediaSuggestion = suggestion.toMediaSuggestion(items)
                    result.add(mediaSuggestion)
                }
            }
        }
        return result.sortedBy { it.order }
    }

    private fun getSuggestions(): List<Suggestion> {
        return _suggestionLocalDataSource.getAll()
    }

    private fun getLocalData(): List<MediaSuggestion> {
        return _suggestionsWithItems
            .map { it.toMediaSuggestion() }
    }

    private suspend fun doRequest(apiPath: String): DataResult<ListContentResponse<MediaItem>> {
        return _remoteDataSource.getMediaItemsResult(apiPath)
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
