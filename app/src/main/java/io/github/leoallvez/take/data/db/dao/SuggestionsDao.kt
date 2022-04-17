package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.MovieSuggestion
import io.github.leoallvez.take.data.model.Suggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionsDao {

    @Insert
    suspend fun insert(vararg suggestions: Suggestion)

    @Query("SELECT * FROM suggestions WHERE type = :type")
    fun getByType(type: String): List<Suggestion>

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM suggestions WHERE type = :type")
    fun getByTypeWithMovies(type: String): List<MovieSuggestion>
}
