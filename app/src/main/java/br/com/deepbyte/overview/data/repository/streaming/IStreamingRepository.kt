package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.api.response.ListResponse
import br.com.deepbyte.overview.data.model.provider.StreamingService
import br.com.deepbyte.overview.data.source.DataResult
import kotlinx.coroutines.flow.Flow

interface IStreamingRepository {
    suspend fun getItems(): Flow<DataResult<ListResponse<StreamingService>>>
}
