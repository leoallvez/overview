package io.github.leoallvez.take.data.source.movie

import io.github.leoallvez.take.data.db.dao.MovieDao
import io.github.leoallvez.take.data.model.Movie
import javax.inject.Inject

class MovieLocalDataSource @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun save(vararg entities: Movie) = dao.insert(*entities)
}
