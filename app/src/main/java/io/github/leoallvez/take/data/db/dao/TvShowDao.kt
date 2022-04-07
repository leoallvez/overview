package io.github.leoallvez.take.data.db.dao

import androidx.room.*
import io.github.leoallvez.take.data.model.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {

    @Insert
    suspend fun insert(model: TvShow)

    @Query("SELECT * FROM tv_shows order by id desc;")
    fun getAll(): Flow<List<TvShow>>

    @Update
    suspend fun update(model: TvShow)

    @Delete
    suspend fun delete(model: TvShow)

    @Query("SELECT * FROM tv_shows WHERE id = :id;")
    fun getById(id: Int): Flow<TvShow>
}