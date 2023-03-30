package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.model.media.MediaType
interface IGenreRepository {
    suspend fun mediaTypeNotCached(): Boolean
    suspend fun insertMediaTypes(mediaTypes: List<MediaType>)
    suspend fun cacheGenreWithType(type: MediaTypeEnum)
}
