package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.api.response.GenreListResponse
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.GenreType
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.genre.GenreLocalDataSource
import br.com.deepbyte.overview.data.source.genre.GenreTypeLocalDataSource
import br.com.deepbyte.overview.data.source.genre.IGenreRemoteDataSource
import javax.inject.Inject

private typealias GenreListResult = DataResult<GenreListResponse>

class GenreRepository @Inject constructor(
    private val _genreLocalSource: GenreLocalDataSource,
    private val _genreRemoteSource: IGenreRemoteDataSource,
    private val _genreTypeLocalSource: GenreTypeLocalDataSource
) : IGenreRepository {
    override suspend fun cacheGenre(mediaType: MediaType) {
        val genres = requestGenre(mediaType)
        _genreLocalSource.insert(*genres.toTypedArray())
    }

    override suspend fun localGenreTypeIsEmpty(): Boolean {
        return _genreTypeLocalSource.isEmpty()
    }

    override suspend fun insertGenreType(vararg genreType: GenreType) {
        _genreTypeLocalSource.insert(*genreType)
    }

    private suspend fun requestGenre(type: MediaType): List<Genre> {
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
