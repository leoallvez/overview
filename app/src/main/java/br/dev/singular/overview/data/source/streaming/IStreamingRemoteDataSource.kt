package br.dev.singular.overview.data.source.streaming

import br.dev.singular.overview.data.model.provider.StreamingEntity

interface IStreamingRemoteDataSource {
    suspend fun getItems(): List<StreamingEntity>
    suspend fun getItems(apiId: Long, type: String): List<StreamingEntity>
}
