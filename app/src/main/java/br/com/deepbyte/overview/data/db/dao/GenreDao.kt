package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.deepbyte.overview.data.model.media.Genre

@Dao
interface GenreDao {

    @Transaction
    fun save(vararg genres: Genre, genreTypeId: Long) {
        val genresDb = getAll()
        val ids = mutableListOf<Long>()
        genres.forEach { genreApi: Genre ->
            val genreDb = genresDb.find { it.apiId == genreApi.apiId }
            genreDb?.let {
                val id = insert(genreApi)
                ids.add(id)
            }
        }
    }

    @Insert
    fun insert(vararg genre: Genre)

    @Insert
    fun insert(genreApi: Genre): Long

    @Query("SELECT * FROM genres")
    fun getAll(): List<Genre>
}
