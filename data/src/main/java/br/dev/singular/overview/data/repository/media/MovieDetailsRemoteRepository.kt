package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.MovieDetails
import br.dev.singular.overview.domain.repository.GetById
import javax.inject.Inject

class MovieDetailsRemoteRepository @Inject constructor(
    private val mediaDataSource: IMediaRemoteDataSource,
    private val extrasRepository: MediaDetailsExtrasRepository,
) : GetById<MovieDetails> {

    override suspend fun getById(id: Long): MovieDetails? {
        val movie = when (val response = mediaDataSource.getMovieById(id)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> return null
        }

        val extras = extrasRepository.fetch(id, MediaDataType.MOVIE, movie.genres)
        return with(receiver = extras) {
            movie.copy(
                videos = videos,
                catalogs = catalogs,
                genres = genres,
            )
        }
    }
}
