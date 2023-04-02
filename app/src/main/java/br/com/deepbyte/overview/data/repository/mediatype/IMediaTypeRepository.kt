package br.com.deepbyte.overview.data.repository.mediatype

import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum

interface IMediaTypeRepository {
    suspend fun notCached(): Boolean
    suspend fun insert(types: List<MediaType>)
    suspend fun getItemWithGenres(type: MediaTypeEnum): List<Genre>
}
