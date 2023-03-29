package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.GenreTypeCrossRef
import br.com.deepbyte.overview.data.model.media.GenreTypeWithGenres

@Dao
interface GenreDao {

    @Transaction
    fun saveGenres(models: List<Genre>, genreType: String) {
        val cached = getGenreTypeWithGenres(genreType)
        models.forEach { genreApi: Genre ->
            val genreCache = cached.genres.find { it.apiId == genreApi.apiId }
            if (genreCache == null) {
                val genreId = insert(genreApi)
                val genreTypeCross = GenreTypeCrossRef(cached.genreType.dbId, genreId)
                saveGenreTypeCross(genreTypeCross)
            }
        }
    }

    @Query("SELECT * FROM genre_types WHERE key = :genreType")
    fun getGenreTypeWithGenres(genreType: String): GenreTypeWithGenres

    @Insert
    fun saveGenreTypeCross(model: GenreTypeCrossRef)

    @Insert
    fun insert(vararg model: Genre)

    @Insert
    fun insert(model: Genre): Long

    @Query("SELECT * FROM genres")
    fun getAll(): List<Genre>
}
