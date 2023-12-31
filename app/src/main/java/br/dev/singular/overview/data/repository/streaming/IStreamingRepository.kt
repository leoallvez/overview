package br.dev.singular.overview.data.repository.streaming

import br.dev.singular.overview.data.model.provider.StreamingData
import br.dev.singular.overview.data.model.provider.StreamingEntity
import kotlinx.coroutines.flow.Flow

interface IStreamingRepository {
    suspend fun getItems(): Flow<List<StreamingEntity>>

    suspend fun getAllSelected(): Flow<List<StreamingEntity>>

    suspend fun getAllLocal(): Flow<StreamingData>

    suspend fun getAllRemote(region: String): List<StreamingEntity>

    suspend fun updateAllLocal(steaming: List<StreamingEntity>)
}
