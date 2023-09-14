package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.db.dao.GenreDao
import br.com.deepbyte.overview.data.model.media.GenreEntity
import javax.inject.Inject

class GenreLocalDataSource @Inject constructor(
    private val _dao: GenreDao
) {
    fun getAll(): List<GenreEntity> = _dao.getAll()

    fun save(models: List<GenreEntity>, mediaType: String) = _dao.saveGenres(models, mediaType)

    fun getGenresWithMediaType(mediaType: String) = _dao.getGenresWithMediaType(mediaType)
}
