package br.dev.singular.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import br.dev.singular.overview.data.model.media.MediaEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MediaDao {

    @Insert(onConflict = REPLACE)
    fun insert(models: List<MediaEntity>)

    @Query("SELECT * FROM medias WHERE api_id = :apiId")
    fun find(apiId: Long): MediaEntity?

    @Query("SELECT * FROM medias WHERE is_liked = 1")
    fun getLiked(): List<MediaEntity>

    @Query("SELECT * FROM medias WHERE is_indicated = 1")
    fun getIndicated(): Flow<List<MediaEntity>>

    @Query("DELETE FROM medias WHERE is_liked = 0")
    fun deleteNotLiked()

    @Query("SELECT is_liked FROM medias WHERE api_id = :apiId")
    fun isLiked(apiId: Long): Boolean

    @Transaction
    fun update(model: MediaEntity) {
        val media = find(model.apiId)
        model.dbId = media?.dbId ?: 0
        model.lastUpdate = Date()
        insert(listOf(model))
    }
}
