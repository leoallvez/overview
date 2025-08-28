package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import br.dev.singular.overview.data.model.MediaDataModel

@Dao
interface MediaDao {

    @Insert(onConflict = REPLACE)
    fun insert(vararg models: MediaDataModel)

    @Update
    fun update(vararg models: MediaDataModel)

    @Query("SELECT * FROM media")
    suspend fun getAll(): List<MediaDataModel>
}
