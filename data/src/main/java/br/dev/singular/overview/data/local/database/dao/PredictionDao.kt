package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.PredictionDataModel

@Dao
interface PredictionDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(model: PredictionDataModel)

    @Query("SELECT * FROM prediction WHERE id = :id AND type = :type")
    suspend fun get(id: Long, type: MediaDataType): PredictionDataModel?

    @Query("DELETE FROM prediction")
    suspend fun deleteAll()
}
