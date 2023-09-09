package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.model.provider.StreamingEntity
import br.com.deepbyte.overview.data.model.provider.StreamingsData
import kotlinx.coroutines.flow.Flow

interface IStreamingRepository {
    suspend fun getItems(): Flow<List<StreamingEntity>>

    suspend fun getAllSelected(): Flow<List<StreamingEntity>>

    // TODO: use this in edit streaming screen;
    suspend fun getStreamingsData(): Flow<StreamingsData>
}
