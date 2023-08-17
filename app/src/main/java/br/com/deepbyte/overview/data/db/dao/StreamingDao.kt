package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.deepbyte.overview.data.model.provider.StreamingEntity

@Dao
interface StreamingDao {
    @Insert
    fun insert(vararg streaming: StreamingEntity)

    @Insert
    fun insert(streaming: List<StreamingEntity>)

    @Update
    fun update(vararg streaming: StreamingEntity)

    @Query("SELECT * FROM streamings ORDER BY display_priority")
    fun getAll(): List<StreamingEntity>

    @Query("SELECT * FROM streamings WHERE selected = 1 ORDER BY display_priority")
    fun getAllSelected(): List<StreamingEntity>

    @Query("DELETE FROM streamings")
    fun deleteAll()

    @Transaction
    fun upgrade(streamings: List<StreamingEntity>) {
        deleteAll()
        insert(streamings)
    }
}
