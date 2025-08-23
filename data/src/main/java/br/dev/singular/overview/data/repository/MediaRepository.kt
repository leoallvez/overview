package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.model.MediaRoute
import br.dev.singular.overview.data.model.toDomainModel
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.GetAllByParam

import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val remoteSource: IMediaRemoteDataSource
) : GetAllByParam<Media, MediaParam> {

    override suspend fun getAll(param: MediaParam): List<Media> {

        val route = MediaRoute.getByKey(param.key)
        if (route == MediaRoute.UNKNOWN) {
            throw RepositoryException("Unknown route for key: ${param.key}")
        }

        return when (val response = remoteSource.getByPath(route.path)) {
            is DataResult.Success -> response.data.results.map { it.toDomainModel() }
            is DataResult.Error -> throw RepositoryException(response.message)
        }
    }
}
