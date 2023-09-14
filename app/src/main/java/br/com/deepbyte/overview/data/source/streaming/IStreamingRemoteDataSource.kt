package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.model.provider.StreamingEntity

interface IStreamingRemoteDataSource {
    suspend fun getItems(): List<StreamingEntity>
    suspend fun getItems(apiId: Long, type: String): List<StreamingEntity>
}
