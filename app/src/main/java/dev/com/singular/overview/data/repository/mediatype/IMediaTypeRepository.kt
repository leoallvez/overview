package dev.com.singular.overview.data.repository.mediatype

import dev.com.singular.overview.data.model.media.MediaTypeEntity

interface IMediaTypeRepository {
    suspend fun notCached(): Boolean
    suspend fun insert(types: List<MediaTypeEntity>)
}
