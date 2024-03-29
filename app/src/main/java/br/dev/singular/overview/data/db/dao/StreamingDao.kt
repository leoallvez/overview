package br.dev.singular.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.dev.singular.overview.data.model.provider.StreamingEntity

@Dao
interface StreamingDao {
    @Insert
    fun insert(vararg model: StreamingEntity)

    @Insert
    fun insert(models: List<StreamingEntity>)

    @Update
    fun update(vararg model: StreamingEntity)

    @Query("SELECT * FROM streamings ORDER BY display_priority")
    fun getAll(): List<StreamingEntity>

    @Query("SELECT * FROM streamings WHERE selected = 1 ORDER BY display_priority")
    fun getAllSelected(): List<StreamingEntity>

    @Query("DELETE FROM streamings")
    fun deleteAll()

    @Transaction
    fun update(models: List<StreamingEntity>) {
        deleteAll()
        insert(models)
    }
}
