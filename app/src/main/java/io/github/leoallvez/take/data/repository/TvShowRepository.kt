package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.AudioVisual
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.Suggestion.Companion.TV_SHOW_TYPE
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.model.TvShow
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
            .getByType(TV_SHOW_TYPE)
    }

    override suspend fun saveCache(
        audioVisuals: List<AudioVisual>,
        suggestionId: Long
    ) {
        val tvShows = audioVisuals as List<TvShow>
        tvShows.forEach { it.suggestionId = suggestionId }
        localDataSource.save(*tvShows.toTypedArray())
    }
}
