package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.db.dao.GenreTypeDao
import br.com.deepbyte.overview.data.model.media.GenreType
import javax.inject.Inject

class GenreTypeLocalDataSource @Inject constructor(
    private val _dao: GenreTypeDao
) {

    fun insert(genreType: List<GenreType>) = _dao.insert(genreType)

    fun isEmpty(): Boolean = _dao.getAll().isEmpty()

    fun getAll(): List<GenreType> = _dao.getAll()
}
