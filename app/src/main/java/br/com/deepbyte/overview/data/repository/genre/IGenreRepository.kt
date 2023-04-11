package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.source.media.MediaTypeEnum

interface IGenreRepository {
    suspend fun cacheWithType(type: MediaTypeEnum)
    suspend fun getItemsByMediaType(type: MediaTypeEnum): List<Genre>
}
