package br.com.deepbyte.overview.data.repository.mediatype

import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.source.media.local.MediaTypeLocalDataSource
import javax.inject.Inject

class MediaTypeRepository @Inject constructor(
    private val _localSource: MediaTypeLocalDataSource
) : IMediaTypeRepository {
    override suspend fun notCached() = _localSource.isEmpty()
    override suspend fun insert(types: List<MediaType>) = _localSource.insert(types)
    override suspend fun getItemWithGenres(type: MediaTypeEnum): List<Genre> {
        return _localSource.getItemWithGenres(type.key).flatMap { it.genres }
    }
}
