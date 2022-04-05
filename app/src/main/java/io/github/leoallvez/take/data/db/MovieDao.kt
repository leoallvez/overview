package io.github.leoallvez.take.db

import androidx.room.*
import io.github.leoallvez.take.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert
    suspend fun insert(task: Movie)

    @Query("SELECT * FROM movies order by id desc;")
    fun getAll(): Flow<List<Movie>>

    @Update
    suspend fun update(task: Movie)

    @Delete
    suspend fun delete(task: Movie)

    @Query("SELECT * FROM movies WHERE id = :id;")
    fun getById(id: Int): Flow<Movie>
}