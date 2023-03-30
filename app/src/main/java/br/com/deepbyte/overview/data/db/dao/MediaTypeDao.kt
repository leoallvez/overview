package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.deepbyte.overview.data.model.media.MediaType

@Dao
interface MediaTypeDao {
    @Insert
    fun insert(genre: List<MediaType>)

    @Query("SELECT * FROM media_types")
    fun getAll(): List<MediaType>
}
