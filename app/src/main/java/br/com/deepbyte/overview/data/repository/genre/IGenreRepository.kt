package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.GenreType

interface IGenreRepository {
    suspend fun cacheGenre(mediaType: MediaType)
    suspend fun localGenreTypeIsEmpty(): Boolean
    suspend fun insertGenreType(vararg genreType: GenreType)
}
