package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.mappers.toDomain
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.MediaParam
import br.dev.singular.overview.domain.repository.Page
import br.dev.singular.overview.domain.repository.GetPage
import javax.inject.Inject

class MediaRemoteRepository @Inject constructor(
    private val mediaSource: IMediaRemoteDataSource,
    private val routeSource: IMediaRouteLocalDataSource
) : GetPage<Media, MediaParam> {

    override suspend fun getPage(param: MediaParam): Page<Media> {
        // TODO: improve this to another type of request.
        val route = routeSource.getByKey(param.key)
        val path = route?.path ?: return Page()

        return when (val response = mediaSource.getByPath(path)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> Page()
        }
    }
}
