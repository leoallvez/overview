package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.model.provider.Streaming

interface IStreamingRemoteDataSource {
    suspend fun getItems(): List<Streaming>
    suspend fun getItems(apiId: Long, type: String): List<Streaming>
}
