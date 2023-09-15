package br.dev.singular.overview.data.repository.mediatype

import br.dev.singular.overview.data.model.media.MediaTypeEntity

interface IMediaTypeRepository {
    suspend fun notCached(): Boolean
    suspend fun insert(types: List<MediaTypeEntity>)
}
