package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.SuggestionDataModel

@Dao
interface SuggestionDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(models: List<SuggestionDataModel>)

    @Query("SELECT * FROM suggestions")
    suspend fun getAll(): List<SuggestionDataModel>

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()
}
