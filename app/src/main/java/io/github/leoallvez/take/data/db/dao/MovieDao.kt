package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.util.removeRepeated

@Dao
interface MovieDao {

    @Transaction
    suspend fun update(vararg models: Movie) {
        val result = models
                .removeRepeated(listToCompare = getAllAsList())
                .toTypedArray()

        insert(*result)
    }

    @Insert
    suspend fun insert(vararg models: Movie)

    @Query("SELECT * FROM movies order by movie_id desc;")
    fun getAllAsList(): List<Movie>
}
