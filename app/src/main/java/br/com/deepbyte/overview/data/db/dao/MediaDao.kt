package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.deepbyte.overview.data.model.media.MediaEntity

@Dao
interface MediaDao {

    @Insert
    fun insert(vararg models: MediaEntity)

    @Query("SELECT * FROM medias")
    fun getAll(): List<MediaEntity>

    @Query("DELETE FROM medias")
    fun deleteAll()
}
