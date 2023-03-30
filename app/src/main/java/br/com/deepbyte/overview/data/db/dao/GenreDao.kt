package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaTypeGenresCrossRef
import br.com.deepbyte.overview.data.model.media.MediaTypeWithGenres

@Dao
interface GenreDao {

    @Transaction
    fun saveGenres(models: List<Genre>, mediaType: String) {
        val cached = getMediaTypeWithGenres(mediaType)
        models.forEach { genreApi: Genre ->
            val genreCache = cached.genres.find { it.apiId == genreApi.apiId }
            if (genreCache == null) {
                val genreId = insert(genreApi)
                val crossResult = MediaTypeGenresCrossRef(cached.mediaType.dbId, genreId)
                saveMediaTypeGenresCross(crossResult)
            }
        }
    }

    @Query("SELECT * FROM media_types WHERE key = :mediaType")
    fun getMediaTypeWithGenres(mediaType: String): MediaTypeWithGenres

    @Insert
    fun saveMediaTypeGenresCross(model: MediaTypeGenresCrossRef)

    @Insert
    fun insert(vararg model: Genre)

    @Insert
    fun insert(model: Genre): Long

    @Query("SELECT * FROM genres")
    fun getAll(): List<Genre>
}
