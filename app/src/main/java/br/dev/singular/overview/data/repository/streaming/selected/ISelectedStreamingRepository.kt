package br.dev.singular.overview.data.repository.streaming.selected

import br.dev.singular.overview.data.model.provider.StreamingEntity
import kotlinx.coroutines.flow.Flow

interface ISelectedStreamingRepository {
    suspend fun getSelectedItem(): Flow<StreamingEntity?>

    suspend fun updateSelected(streaming: StreamingEntity?)
}
