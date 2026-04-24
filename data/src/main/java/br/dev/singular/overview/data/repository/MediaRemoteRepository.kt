package br.dev.singular.overview.data.repository

import br.dev.singular.overview.data.local.source.IMediaRouteLocalDataSource
import br.dev.singular.overview.data.network.source.DataResult
import br.dev.singular.overview.data.network.source.IMediaRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.QueryState
import br.dev.singular.overview.domain.repository.GetPage
import br.dev.singular.overview.domain.repository.Page
import javax.inject.Inject

class MediaRemoteRepository @Inject constructor(
    private val mediaSource: IMediaRemoteDataSource,
    private val routeSource: IMediaRouteLocalDataSource
) : GetPage<Media, QueryState> {

    override suspend fun getPage(param: QueryState): Page<Media> {

        val route = routeSource.getByKey(key = param.key)
        val path = route?.path ?: return Page()
        val query = param.toData(path)

        return when (val response = mediaSource.getByQuery(query, route.params)) {
            is DataResult.Success -> response.data.toDomain()
            is DataResult.Error -> Page()
        }
    }
}
