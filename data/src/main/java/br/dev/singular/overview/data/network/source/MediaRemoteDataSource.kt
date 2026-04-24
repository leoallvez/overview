package br.dev.singular.overview.data.network.source

import br.dev.singular.overview.data.model.MediaDataPage
import br.dev.singular.overview.data.model.QueryDataState
import br.dev.singular.overview.data.network.ApiService
import br.dev.singular.overview.data.util.mappers.toParams
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getByQuery(
        queryState: QueryDataState,
        extraParams: Map<String, String>
    ): DataResult<MediaDataPage>
}

class MediaRemoteDataSource @Inject constructor(
    private val api: ApiService
) : IMediaRemoteDataSource {

    override suspend fun getByQuery(
        queryState: QueryDataState,
        extraParams: Map<String, String>
    ) = with(queryState) {
        val response = api.fetchMediaPage(
            path = path,
            page = page.takeIf { it > 0 },
            query = query.takeIf { it.isNotEmpty() },
            options = queryState.toParams(extraParams).filterValues { it.isNotEmpty() },
        )
        responseToResult(response)
    }
}
