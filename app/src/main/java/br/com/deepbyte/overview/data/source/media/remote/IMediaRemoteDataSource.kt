package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult

interface IMediaRemoteDataSource<T : Media> {
    suspend fun find(apiId: Long): DataResult<T>

    suspend fun getAllBySuffix(
        page: Int,
        watchProviders: List<Long> = emptyList()
    ): List<T>

    suspend fun search(query: String): DataResult<ListResponse<T>>
}
