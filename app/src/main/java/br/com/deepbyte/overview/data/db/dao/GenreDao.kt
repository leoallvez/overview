package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.deepbyte.overview.data.model.media.Genre

@Dao
interface GenreDao {
    @Insert
    fun insert(vararg genre: Genre)

    @Query("SELECT * FROM genres")
    fun getAll(): List<Genre>
}
