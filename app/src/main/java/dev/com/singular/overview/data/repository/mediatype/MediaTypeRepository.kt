package dev.com.singular.overview.data.repository.mediatype

import dev.com.singular.overview.data.source.media.local.MediaTypeLocalDataSource
import dev.com.singular.overview.data.model.media.MediaTypeEntity
import javax.inject.Inject

class MediaTypeRepository @Inject constructor(
    private val _localSource: MediaTypeLocalDataSource
) : IMediaTypeRepository {
    override suspend fun notCached() = _localSource.isEmpty()
    override suspend fun insert(types: List<MediaTypeEntity>) = _localSource.insert(types)
}
