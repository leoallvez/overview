package dev.com.singular.overview.data.repository.media.interfaces

import dev.com.singular.overview.data.model.media.MediaEntity
import kotlinx.coroutines.flow.Flow

interface IMediaCacheRepository {
    suspend fun saveMediaCache(): Boolean
    suspend fun getMediaCache(): Flow<List<MediaEntity>>
}
