package dev.com.singular.overview.data.repository.streaming

import dev.com.singular.overview.data.model.provider.StreamingEntity
import dev.com.singular.overview.data.model.provider.StreamingsData
import kotlinx.coroutines.flow.Flow

interface IStreamingRepository {
    suspend fun getItems(): Flow<List<StreamingEntity>>

    suspend fun getAllSelected(): Flow<List<StreamingEntity>>

    // TODO: use this in edit streaming screen;
    suspend fun getStreamingsData(): Flow<StreamingsData>
}
