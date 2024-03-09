package br.dev.singular.overview.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import br.dev.singular.overview.data.model.media.MediaEntity
import java.util.Date

@Dao
interface MediaDao {

    @Insert(onConflict = REPLACE)
    fun insert(models: List<MediaEntity>)

    @Query("SELECT * FROM medias WHERE api_id = :apiId")
    fun find(apiId: Long): MediaEntity?

    @Query("SELECT * FROM medias WHERE is_liked = 1 ORDER BY last_update DESC")
    fun getAllLiked(): PagingSource<Int, MediaEntity>

    @Query("SELECT * FROM medias WHERE is_liked = 1 AND type = :type ORDER BY last_update DESC")
    fun getAllLikedByType(type: String): PagingSource<Int, MediaEntity>

    @Query("SELECT is_liked FROM medias WHERE api_id = :apiId")
    fun isLiked(apiId: Long): Boolean

    @Query("DELETE FROM medias WHERE is_liked = 0 AND last_update < :date")
    fun deleteUnlikedOlderThan(date: Date)

    @Transaction
    fun update(model: MediaEntity) {
        val media = find(model.apiId)
        media?.apply { model.dbId = dbId }
        model.lastUpdate = Date()
        insert(listOf(model))
    }
}
