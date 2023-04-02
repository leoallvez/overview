package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.deepbyte.overview.data.model.media.MediaType
import br.com.deepbyte.overview.data.model.media.MediaTypeWithGenres

@Dao
interface MediaTypeDao {
    @Insert
    fun insert(genre: List<MediaType>)

    @Query("SELECT * FROM media_types")
    fun getAll(): List<MediaType>

    @Transaction
    @Query("SELECT * FROM media_types WHERE key = :key")
    fun getItemWithGenres(key: String): List<MediaTypeWithGenres>
}
