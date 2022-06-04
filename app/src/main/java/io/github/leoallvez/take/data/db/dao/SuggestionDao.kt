package io.github.leoallvez.take.data.db.dao

import androidx.room.*
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.model.Suggestion

@Dao
interface SuggestionDao {

    @Transaction
    suspend fun update(vararg models: Suggestion) {
        deleteAll()
        insert(*models)
    }

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(vararg models: Suggestion)

    @Query("SELECT * FROM suggestions")
    fun getAll(): List<Suggestion>

    @Query("""SELECT *
              FROM suggestions AS s
              JOIN media_items AS m ON s.suggestion_db_id = m.suggestion_id""")
    fun getAllWithMediaItems(): Map<Suggestion, List<MediaItem>>
}
