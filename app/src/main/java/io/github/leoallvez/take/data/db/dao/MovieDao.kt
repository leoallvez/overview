package io.github.leoallvez.take.data.db.dao

import androidx.room.*
import io.github.leoallvez.take.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert
    suspend fun insert(vararg model: Movie)

    @Query("SELECT * FROM movies order by movie_id desc;")
    fun getAll(): Flow<List<Movie>>

    @Update
    suspend fun update(model: Movie)

    @Delete
    suspend fun delete(model: Movie)

    @Query("SELECT * FROM movies WHERE movie_id = :id;")
    fun getById(id: Int): Flow<Movie>
}
