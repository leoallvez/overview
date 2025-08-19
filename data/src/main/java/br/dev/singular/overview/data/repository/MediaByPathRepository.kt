package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.model.toDomainModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.GetAllByParam
import javax.inject.Inject

class MediaByPathRepository @Inject constructor(
    private val remoteSource: IMediaRemoteDataSource
) : GetAllByParam<Media, String> {

    override suspend fun getAll(param: String): List<Media> {
        return when (val response = remoteSource.getByPath(param)) {
            is DataResult.Success -> response.data.results.map { it.toDomainModel() }
            is DataResult.Error -> throw RepositoryException(response.message)
        }
    }
}
