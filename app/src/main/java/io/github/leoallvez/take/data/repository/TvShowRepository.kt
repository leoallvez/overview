package io.github.leoallvez.take.data.repository

import io.github.leoallvez.take.data.model.Audiovisual
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.SuggestionResult
import io.github.leoallvez.take.data.model.TvShow
import io.github.leoallvez.take.data.source.AudiovisualResult
import io.github.leoallvez.take.data.source.suggestion.SuggestionLocalDataSource
import io.github.leoallvez.take.data.source.tvshow.TvShowLocalDataSource
import io.github.leoallvez.take.data.source.tvshow.TvShowRemoteDataSource
import io.github.leoallvez.take.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val localDataSource: TvShowLocalDataSource,
    private val remoteDataSource: TvShowRemoteDataSource,
    private val suggestionLocalDataSource: SuggestionLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): AudioVisualRepository(ioDispatcher) {

    override fun hasCache(): Boolean {
        return suggestionLocalDataSource.hasTvShowsCache()
    }

    override fun getLocalData(): List<SuggestionResult> {
        return suggestionLocalDataSource
            .getWithTvShows()
    }

    override suspend fun doRequest(apiPath: String): AudiovisualResult {
        return remoteDataSource.get(apiPath)
    }

    override fun getSuggestions(): List<Suggestion> {
        return suggestionLocalDataSource.getByType(Suggestion.MOVIE_TYPE)
    }

    override suspend fun saveCache(
        audiovisuals: List<Audiovisual>,
        suggestionId: Long
    ) {
        val tvShows = audiovisuals as List<TvShow>
        tvShows.forEach { it.suggestionId = suggestionId}
        localDataSource.save(*tvShows.toTypedArray())
    }
}
