package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.CatalogDataModel

@Dao
interface CatalogDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg models: CatalogDataModel)

    @Query("SELECT * FROM catalog ORDER BY priority")
    suspend fun getAll(): List<CatalogDataModel>

    @Delete
    suspend fun delete(vararg models: CatalogDataModel)
}
