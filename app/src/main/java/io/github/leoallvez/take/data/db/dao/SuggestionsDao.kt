package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.Movie
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.TvShow

@Dao
interface SuggestionsDao {

    @Transaction
    suspend fun update(vararg suggestions: Suggestion) {
        deleteAll()
        insert(*suggestions)
    }

    @Insert
    suspend fun insert(vararg suggestions: Suggestion)

    @Query("SELECT * FROM suggestions WHERE type = :type")
    fun getByType(type: String): List<Suggestion>

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

    @Query(
        """SELECT * FROM suggestions AS s
                 JOIN movies AS m ON s.suggestion_id = m.suggestion_id""")
    fun getWithMovies(): Map<Suggestion, List<Movie>>

    @Query(
        """SELECT * FROM suggestions AS s
                 JOIN tv_shows AS t ON s.suggestion_id = t.suggestion_id""")
    fun getWithTvShows(): Map<Suggestion, List<TvShow>>
}
