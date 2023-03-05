package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.api.response.PagingResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult

interface IMediaRemoteDataSource<T : Media> {
    suspend fun find(apiId: Long): DataResult<T>

    suspend fun pagingAllBySuffix(
        page: Int,
        urlSuffix: String,
        providerId: Long? = null
    ): DataResult<PagingResponse<T>>

    suspend fun search(query: String): DataResult<ListResponse<T>>
}
