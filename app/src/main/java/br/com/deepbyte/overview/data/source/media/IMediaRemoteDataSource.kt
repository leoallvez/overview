package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult

interface IMediaRemoteDataSource <T : Media> {
    suspend fun find(apiId: Long): DataResult<T>
    suspend fun getItemsByUrl(url: String): DataResult<ListResponse<T>>
    suspend fun search(query: String): DataResult<ListResponse<T>>
}
