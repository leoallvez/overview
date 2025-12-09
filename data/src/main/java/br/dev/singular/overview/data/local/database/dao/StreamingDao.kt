package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.StreamingDataModel

@Dao
interface StreamingDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg models: StreamingDataModel)

    @Query("SELECT * FROM streaming ORDER BY priority")
    suspend fun getAll(): List<StreamingDataModel>

    @Delete
    suspend fun delete(vararg models:StreamingDataModel)
}
