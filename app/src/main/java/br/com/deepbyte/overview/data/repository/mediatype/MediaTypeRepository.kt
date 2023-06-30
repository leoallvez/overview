package br.com.deepbyte.overview.data.repository.mediatype

import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.source.media.local.MediaTypeLocalDataSource
import javax.inject.Inject

class MediaTypeRepository @Inject constructor(
    private val _localSource: MediaTypeLocalDataSource
) : IMediaTypeRepository {
    override suspend fun notCached() = _localSource.isEmpty()
    override suspend fun insert(types: List<MediaType>) = _localSource.insert(types)
}
