package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import br.com.deepbyte.overview.data.model.provider.Streaming

@Dao
interface StreamingDao {
    @Insert
    fun insert(vararg streaming: Streaming)
}
