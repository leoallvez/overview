package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.GenreDataModel
import br.dev.singular.overview.data.model.MediaDataType
import br.dev.singular.overview.data.model.MediaTypeGenreDataModel

@Dao
interface GenreDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg models: GenreDataModel)

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg models: MediaTypeGenreDataModel)

    @Query(
        """
        SELECT g.* FROM genre AS g
        INNER JOIN media_type_genre AS mtg
        ON g.id = mtg.genre_id
        WHERE mtg.type = :type
    """
    )
    suspend fun getByMediaType(type: MediaDataType): List<GenreDataModel>
}
