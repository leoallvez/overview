package br.com.deepbyte.overview.data.repository.genre

import br.com.deepbyte.overview.data.MediaType
import br.com.deepbyte.overview.data.model.media.GenreType

interface IGenreRepository {
    suspend fun localGenreTypeIsEmpty(): Boolean
    suspend fun insertGenreType(genreType: List<GenreType>)
    suspend fun cacheGenre(mediaType: MediaType)
}
