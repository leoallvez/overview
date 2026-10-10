package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.TvShowDetails
import br.dev.singular.overview.domain.repository.GetById
import javax.inject.Inject

class TvShowDetailsRemoteRepository @Inject constructor(
    private val mediaDataSource: IMediaRemoteDataSource,
    private val extrasRepository: MediaDetailsExtrasRepository,
) : GetById<TvShowDetails> {

    override suspend fun getById(id: Long): TvShowDetails? {
        val tvShow = when (val response = mediaDataSource.getTvShowById(id)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> return null
        }

        val extras = extrasRepository.fetch(id, MediaDataType.TV, tvShow.genres)
        return with(receiver = extras) {
            tvShow.copy(
                videos = videos,
                catalogs = catalogs,
                genres = genres,
            )
        }
    }
}
