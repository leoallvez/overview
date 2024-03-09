package br.dev.singular.overview.data.repository.genre

import br.dev.singular.overview.data.model.media.GenreEntity
import br.dev.singular.overview.data.source.media.MediaType

interface IGenreRepository {
    suspend fun cacheWithType(type: MediaType)
    suspend fun getItemsByMediaType(type: MediaType): List<GenreEntity>
}
