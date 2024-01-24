package br.dev.singular.overview.data.repository.genre

import br.dev.singular.overview.data.api.response.GenreListResponse
import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.source.DataResult
import br.dev.singular.overview.data.source.genre.GenreLocalDataSource
import br.dev.singular.overview.data.source.genre.IGenreRemoteDataSource
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.data.source.media.MediaType.MOVIE
import br.dev.singular.overview.data.source.media.MediaType.TV_SHOW
import javax.inject.Inject

private typealias GenreListResult = DataResult<GenreListResponse>

class GenreRepository @Inject constructor(
    private val _localSource: GenreLocalDataSource,
    private val _remoteSource: IGenreRemoteDataSource
) : IGenreRepository {

    override suspend fun cacheWithType(type: MediaType) {
        val genres = requestData(type)
        _localSource.save(genres, type.key)
    }

    override suspend fun getItemsByMediaType(type: MediaType) = if (type.isDefault()) {
        getMergedGenres()
    } else {
        filterGenres(type.key)
    }

    private fun getMergedGenres() =
        (filterGenres(MOVIE.key) + filterGenres(TV_SHOW.key)).distinctBy { it.apiId }

    private fun filterGenres(type: String) =
        _localSource.getGenresWithMediaType(type).flatMap { it.genres }

    private suspend fun requestData(type: MediaType): List<GenreEntity> {
        val result = _remoteSource.getItemByMediaType(type)
        return getGenreList(result)
    }

    private fun getGenreList(result: GenreListResult): List<GenreEntity> {
        return if (result is DataResult.Success) {
            result.data?.genres ?: listOf()
        } else {
            listOf()
        }
    }
}
