package br.dev.singular.overview.data.repository.mediatype

import br.dev.singular.overview.data.model.media.MediaTypeEntity
import br.dev.singular.overview.data.source.media.local.MediaTypeLocalDataSource
import javax.inject.Inject

class MediaTypeRepository @Inject constructor(
    private val _localSource: MediaTypeLocalDataSource
) : IMediaTypeRepository {
    override suspend fun notCached() = _localSource.isEmpty()
    override suspend fun insert(types: List<MediaTypeEntity>) = _localSource.insert(types)
}
