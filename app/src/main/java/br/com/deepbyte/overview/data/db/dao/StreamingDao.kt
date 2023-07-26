package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.deepbyte.overview.data.model.provider.Streaming
import kotlinx.coroutines.flow.Flow

@Dao
interface StreamingDao {
    @Insert
    fun insert(vararg streaming: Streaming)

    @Update
    fun update(vararg streaming: Streaming)

    @Query("SELECT * FROM streamings")
    fun getAll(): List<Streaming>

    @Query("SELECT * FROM streamings WHERE selected = 1 ORDER BY display_priority")
    fun getAllSelected(): Flow<List<Streaming>>

    @Query("DELETE FROM streamings")
    fun deleteAll()
}
