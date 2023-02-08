package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.model.provider.StreamingService

interface IStreamingRemoteDataSource {
    suspend fun getItems(): List<StreamingService>
    suspend fun getItems(apiId: Long, type: String): List<StreamingService>
}
