package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.dev.singular.overview.data.model.PendingFavoriteDataModel

@Dao
interface PendingFavoriteDao {

    @Query("SELECT * FROM pending_favorite ORDER BY last_update DESC")
    suspend fun getAll(): List<PendingFavoriteDataModel>

    @Query("DELETE FROM pending_favorite")
    suspend fun deleteAll()
}
