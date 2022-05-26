package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.data.model.Suggestion

@Dao
interface SuggestionsDao {

    @Transaction
    suspend fun update(vararg suggestions: Suggestion) {
        deleteAll()
        insert(*suggestions)
    }

    @Query("SELECT * FROM suggestions")
    fun getAll(): List<Suggestion>

    @Insert
    suspend fun insert(vararg suggestions: Suggestion)

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

    @Query(
        """SELECT * FROM suggestions AS s
                 JOIN audio_visual_items AS m ON s.db_id = m.suggestion_id""")
    fun getWithAudioVisualItem(): Map<Suggestion, List<AudioVisualItem>>
}
