package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.deepbyte.overview.data.model.media.GenreType

@Dao
interface GenreTypeDao {
    @Insert
    fun insert(vararg genre: GenreType)

    @Query("SELECT * FROM genre_types")
    fun getAll(): List<GenreType>
}
