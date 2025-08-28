package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.MediaDataModel

@Dao
interface MediaDao {

    @Insert(onConflict = REPLACE)
    fun insert(models: List<MediaDataModel>)

    @Query("SELECT * FROM media ORDER BY last_update DESC")
    suspend fun getAll(): List<MediaDataModel>
}
