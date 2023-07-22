package br.com.deepbyte.overview.data.repository.streaming

import br.com.deepbyte.overview.data.model.provider.Streaming
import kotlinx.coroutines.flow.Flow

interface IStreamingRepository {
    suspend fun getItems(): Flow<List<Streaming>>

    suspend fun getAllSelected(): Flow<List<Streaming>>

    suspend fun itemsFilteredByCurrentCountry(): Flow<List<Streaming>>
}
