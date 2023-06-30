package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.db.dao.GenreDao
import br.com.deepbyte.overview.data.model.media.Genre
import javax.inject.Inject

class GenreLocalDataSource @Inject constructor(
    private val _dao: GenreDao
) {
    fun isEmpty(): Boolean = _dao.getAll().isEmpty()

    fun getAll(): List<Genre> = _dao.getAll()

    fun save(models: List<Genre>, mediaType: String) = _dao.saveGenres(models, mediaType)

    fun getGenresWithMediaType(mediaType: String) = _dao.getGenresWithMediaType(mediaType)
}
