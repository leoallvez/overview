package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.mappers.toDomainModel
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.GetAllByParam
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val mediaRemoteSource: IMediaRemoteDataSource,
    private val mediaLocalSource: IMediaLocalDataSource,
    private val routeLocalSource: IMediaRouteLocalDataSource
) : GetAllByParam<Media, MediaParam>, GetAll<Media> {

    override suspend fun getAllByParam(param: MediaParam): List<Media> {
        val route = routeLocalSource.getByKey(param.key) ?: return emptyList()
        // TODO: improve this to another type of request.
        return when (val response = mediaRemoteSource.getByPath(route.path)) {
            is DataResult.Success -> response.data.results.map { it.toDomainModel() }
            is DataResult.Error -> emptyList()
        }
    }

    override suspend fun getAll() = mediaLocalSource.getAll().map { it.toDomainModel() }
}
