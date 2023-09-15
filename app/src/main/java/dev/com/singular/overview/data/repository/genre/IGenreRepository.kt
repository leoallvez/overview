package dev.com.singular.overview.data.repository.genre

import dev.com.singular.overview.data.model.media.GenreEntity
import dev.com.singular.overview.data.source.media.MediaTypeEnum

interface IGenreRepository {
    suspend fun cacheWithType(type: MediaTypeEnum)
    suspend fun getItemsByMediaType(type: MediaTypeEnum): List<GenreEntity>
}
