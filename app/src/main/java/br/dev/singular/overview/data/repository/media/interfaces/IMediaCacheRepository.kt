package br.dev.singular.overview.data.repository.media.interfaces

import br.dev.singular.overview.data.model.media.MediaEntity
import kotlinx.coroutines.flow.Flow

interface IMediaCacheRepository {
    suspend fun saveMediaCache(): Boolean
    suspend fun getMediaCache(): Flow<List<MediaEntity>>
}
