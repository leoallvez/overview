package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.MovieSuggestion
import io.github.leoallvez.take.data.model.Suggestion
import io.github.leoallvez.take.data.model.Suggestion.Companion.MOVIE_TYPE
import io.github.leoallvez.take.data.model.Suggestion.Companion.TV_SHOW_TYPE
import io.github.leoallvez.take.data.model.TvShowSuggestion
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
    fun getWithMovies(type: String = MOVIE_TYPE): List<MovieSuggestion>

    @Transaction
    @Query("SELECT * FROM suggestions WHERE type = :type")
    fun getWithTvShows(type: String = TV_SHOW_TYPE): List<TvShowSuggestion>
}
