package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.Suggestion

@Dao
interface SuggestionDao {

    @Transaction
    suspend fun update(vararg suggestions: Suggestion) {
        deleteAll()
        insert(*suggestions)
    }

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(vararg suggestions: Suggestion)

    @Query("SELECT * FROM suggestions")
    fun getAll(): List<Suggestion>

    @Query("""SELECT * 
              FROM suggestions AS s
              JOIN media_items AS m ON m.suggestion_id = s.db_id""")
    fun getWithAudioVisualItems(): Map<Suggestion, List<MediaItem>>
}
