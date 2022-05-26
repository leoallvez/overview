package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.AudioVisualItem.Companion.TV_TYPE
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.data.source.tvshow.TvShowLocalDataSource
import io.github.leoallvez.take.data.source.tvshow.TvShowRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import io.github.leoallvez.take.util.toSuggestionResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val localDataSource: TvShowLocalDataSource,
    private val remoteDataSource: TvShowRemoteDataSource,
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : AudioVisualRepository(ioDispatcher) {

    override fun hasCache(): Boolean {
        return suggestionLocalDataSource
            .hasTvShowsCache()
    }

    override fun getLocalData(): List<SuggestionResult> {
        return suggestionLocalDataSource
            .getWithTvShows()
            .map { it.toSuggestionResult() }
    }

    override suspend fun doRequest(apiPath: String): AudiovisualResult {
        return remoteDataSource
            .get(apiPath)
    }

    override fun getSuggestions(): List<Suggestion> {
        return suggestionLocalDataSource
            .getByType(TV_TYPE)
    }

    override suspend fun saveCache(
        audioVisuaItems: List<AudioVisualItem>,
        suggestionId: Long
    ) {
        audioVisuaItems.forEach {
            it.suggestionId = suggestionId
            it.type = "tv"
        }
        localDataSource.update(*audioVisuaItems.toTypedArray())
    }
}
