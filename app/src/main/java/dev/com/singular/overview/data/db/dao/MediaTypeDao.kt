package dev.com.singular.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.com.singular.overview.data.model.media.MediaTypeEntity

@Dao
interface MediaTypeDao {
    @Insert
    fun insert(models: List<MediaTypeEntity>)

    @Query("SELECT * FROM media_types")
    fun getAll(): List<MediaTypeEntity>
}
