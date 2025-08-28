package br.dev.singular.overview.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import br.dev.singular.overview.data.model.MediaDataModel
import br.dev.singular.overview.data.model.MediaDataType
import java.util.Date

@Dao
interface MediaDao {

    @Query("SELECT * FROM media ORDER BY last_update DESC")
    suspend fun getAll(): List<MediaDataModel>

    @Query("""
        SELECT *
        FROM media
        WHERE (:id = 0 OR id = :id)
          AND (:type = 'all' OR type = :type)
          AND (:isLiked IS NULL OR is_liked = :isLiked)
        ORDER BY last_update DESC
        LIMIT :limit
        OFFSET :offset
    """)
    suspend fun getPage(
        id: Long = 0,
        type: MediaDataType = MediaDataType.ALL,
        isLiked: Boolean? = null,
        limit: Int = Int.MAX_VALUE,
        offset: Int = 0
    ): List<MediaDataModel>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg models: MediaDataModel)

    @Transaction
    suspend fun update(model: MediaDataModel) {
        model.lastUpdate = Date()
        insert(model)
    }

    @Delete
    suspend fun delete(vararg models: MediaDataModel)
}
