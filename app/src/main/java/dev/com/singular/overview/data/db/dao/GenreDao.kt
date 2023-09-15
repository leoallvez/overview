package dev.com.singular.overview.data.db.dao

import androidx.room.*
import dev.com.singular.overview.data.model.media.GenreEntity
import dev.com.singular.overview.data.model.media.MediaTypeGenresCrossRef
import dev.com.singular.overview.data.model.media.MediaTypeWithGenres

@Dao
interface GenreDao {

    @Transaction
    fun saveGenres(models: List<GenreEntity>, mediaType: String) {
        val type = getMediaTypeWithGenres(mediaType)?.mediaType
        type?.let {
            models.forEach { genreApi: GenreEntity ->
                val genreDbId = update(genreApi)
                val crossRef = MediaTypeGenresCrossRef(type.dbId, genreDbId)
                saveMediaTypeGenresCross(crossRef)
            }
        }
    }

    @Transaction
    fun update(model: GenreEntity): Long {
        val genreCache = findByApiId(model.apiId)
        return genreCache?.dbId ?: insert(model)
    }

    @Transaction
    @Query("SELECT * FROM media_types m WHERE m.`key` = :mediaType")
    fun getMediaTypeWithGenres(mediaType: String): MediaTypeWithGenres?

    @Transaction
    @Query("SELECT * FROM media_types m WHERE m.`key` = :mediaType")
    fun getGenresWithMediaType(mediaType: String): List<MediaTypeWithGenres>

    @Insert
    fun saveMediaTypeGenresCross(model: MediaTypeGenresCrossRef)

    @Insert
    fun insert(model: GenreEntity): Long

    @Query("SELECT * FROM genres")
    fun getAll(): List<GenreEntity>

    @Query("SELECT * FROM genres WHERE api_id = :apiId")
    fun findByApiId(apiId: Long): GenreEntity?
}
