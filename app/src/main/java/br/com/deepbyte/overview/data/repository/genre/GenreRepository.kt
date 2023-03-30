package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.genre.GenreLocalDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.source.media.local.MediaTypeLocalDataSource
import javax.inject.Inject

private typealias GenreListResult = DataResult<GenreListResponse>

class GenreRepository @Inject constructor(
    private val _genreLocalSource: GenreLocalDataSource,
    private val _genreRemoteSource: IGenreRemoteDataSource,
    private val _mediaTypeLocalSource: MediaTypeLocalDataSource
) : IGenreRepository {

    override suspend fun cacheGenreWithType(type: MediaTypeEnum) {
        val genres = requestGenre(type)
        _genreLocalSource.save(genres, type.key)
    }

    override suspend fun mediaTypeNotCached(): Boolean {
        return _mediaTypeLocalSource.isEmpty()
    }

    override suspend fun insertMediaTypes(mediaTypes: List<MediaType>) {
        _mediaTypeLocalSource.insert(mediaTypes)
    }

    private suspend fun requestGenre(type: MediaTypeEnum): List<Genre> {
        val result = _genreRemoteSource.getItemByMediaType(type)
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
