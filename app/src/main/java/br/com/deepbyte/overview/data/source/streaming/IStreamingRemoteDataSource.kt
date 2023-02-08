package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.provider.StreamingService
import br.com.deepbyte.overview.data.source.DataResult

interface IStreamingRemoteDataSource {
    suspend fun getItems(): DataResult<ListResponse<StreamingService>>
}
