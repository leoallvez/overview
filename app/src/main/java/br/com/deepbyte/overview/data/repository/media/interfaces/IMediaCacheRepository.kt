package br.com.deepbyte.overview.data.repository.media.interfaces

import br.com.deepbyte.overview.data.model.media.MediaEntity
import kotlinx.coroutines.flow.Flow

interface IMediaCacheRepository {
    suspend fun saveMediaCache(): Boolean
    suspend fun getMediaCache(): Flow<List<MediaEntity>>
}
