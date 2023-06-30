package br.com.deepbyte.overview.data.repository.mediatype

import br.com.deepbyte.overview.data.model.media.MediaType

interface IMediaTypeRepository {
    suspend fun notCached(): Boolean
    suspend fun insert(types: List<MediaType>)
}
