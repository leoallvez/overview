package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.deepbyte.overview.data.model.provider.Streaming

@Dao
interface StreamingDao {
    @Insert
    fun insert(vararg streaming: Streaming)

    @Update
    fun update(vararg streaming: Streaming)

    @Query("SELECT * FROM streamings")
    fun getAll(): List<Streaming>
}
