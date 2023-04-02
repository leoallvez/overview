package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.genre.GenreLocalDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import javax.inject.Inject

private typealias GenreListResult = DataResult<GenreListResponse>

class GenreRepository @Inject constructor(
    private val _localSource: GenreLocalDataSource,
    private val _remoteSource: IGenreRemoteDataSource
) : IGenreRepository {

    override suspend fun cacheWithType(type: MediaTypeEnum) {
        val genres = requestData(type)
        _localSource.save(genres, type.key)
    }

    private suspend fun requestData(type: MediaTypeEnum): List<Genre> {
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
