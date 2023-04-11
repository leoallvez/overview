package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.genre.GenreLocalDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum.*
import javax.inject.Inject
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum as MediaType

private typealias GenreListResult = DataResult<GenreListResponse>

class GenreRepository @Inject constructor(
    private val _localSource: GenreLocalDataSource,
    private val _remoteSource: IGenreRemoteDataSource
) : IGenreRepository {

    override suspend fun cacheWithType(type: MediaType) {
        val genres = requestData(type)
        _localSource.save(genres, type.key)
    }

    override suspend fun getItemsByMediaType(type: MediaType) = if (type == ALL) {
        getMergedGenres()
    } else {
        filterGenres(type.key)
    }

    private fun getMergedGenres() = filterGenres(MOVIE.key)
        .filter { filterGenres(TV_SHOW.key).contains(it) }

    private fun filterGenres(type: String) =
        _localSource.getGenresWithMediaType(type).flatMap { it.genres }

    private suspend fun requestData(type: MediaType): List<Genre> {
        val result = _remoteSource.getItemByMediaType(type)
        return getGenreList(result)
    }

    private fun getGenreList(result: GenreListResult): List<Genre> {
        return if (result is DataResult.Success) {
            result.data?.genres ?: listOf()
        } else {
            listOf()
        }
    }
}
