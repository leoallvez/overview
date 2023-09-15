package br.dev.singular.overview.data.repository.genre

import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.source.media.MediaTypeEnum

interface IGenreRepository {
    suspend fun cacheWithType(type: MediaTypeEnum)
    suspend fun getItemsByMediaType(type: MediaTypeEnum): List<GenreEntity>
}
