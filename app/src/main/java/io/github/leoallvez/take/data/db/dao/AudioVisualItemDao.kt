package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.AudioVisualItem
import io.github.leoallvez.take.util.removeRepeated

@Dao
interface AudioVisualItemDao {

    @Transaction
    suspend fun update(vararg models: AudioVisualItem) {
        val result = models
                .removeRepeated(itemsToRemove = getAllAsList())
                .toTypedArray()

        insert(*result)
    }

    @Insert
    suspend fun insert(vararg models: AudioVisualItem)

    @Query("SELECT * FROM audio_visual_items order by db_id desc;")
    fun getAllAsList(): List<AudioVisualItem>
}
