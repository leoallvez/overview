package br.com.deepbyte.overview.data.db.dao

import androidx.room.*
import br.com.deepbyte.overview.data.model.media.Genre
import br.com.deepbyte.overview.data.model.media.MediaTypeGenresCrossRef
import br.com.deepbyte.overview.data.model.media.MediaTypeWithGenres

@Dao
interface GenreDao {

    @Transaction
    fun saveGenres(models: List<Genre>, mediaType: String) {
        val type = getMediaTypeWithGenres(mediaType)?.mediaType
        type?.let {
            models.forEach { genreApi: Genre ->
                val genreDbId = update(genreApi)
                val crossRef = MediaTypeGenresCrossRef(type.dbId, genreDbId)
                saveMediaTypeGenresCross(crossRef)
            }
        }
    }

    @Transaction
    private fun update(model: Genre): Long {
        val genreCache = findByApiId(model.apiId)
        return genreCache?.dbId ?: insert(model)
    }

    @Query("SELECT * FROM media_types m WHERE m.`key` = :mediaType")
    fun getMediaTypeWithGenres(mediaType: String): MediaTypeWithGenres?

    @Transaction
    @Query("SELECT * FROM media_types m WHERE m.`key` = :mediaType")
    fun getGenresWithMediaType(mediaType: String): List<MediaTypeWithGenres>

    @Insert
    fun saveMediaTypeGenresCross(model: MediaTypeGenresCrossRef)

    @Insert
    fun insert(model: Genre): Long

    @Query("SELECT * FROM genres")
    fun getAll(): List<Genre>

    @Query("SELECT * FROM genres WHERE api_id = :apiId")
    fun findByApiId(apiId: Long): Genre?
}
