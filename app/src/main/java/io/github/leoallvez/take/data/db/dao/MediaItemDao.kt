package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.util.removeRepeated

@Dao
interface MediaItemDao {

    @Transaction
    suspend fun update(vararg models: MediaItem) {
        val result = models
                .removeRepeated(itemsToRemove = getAll())
                .toTypedArray()

        insert(*result)
    }

    @Insert
    suspend fun insert(vararg models: MediaItem)

    @Query("SELECT * FROM media_items")
    fun getAll(): List<MediaItem>
}
