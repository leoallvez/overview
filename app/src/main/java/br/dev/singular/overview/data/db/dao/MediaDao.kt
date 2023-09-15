package br.dev.singular.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.dev.singular.overview.data.model.media.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {

    @Insert(onConflict = REPLACE)
    fun insert(models: List<MediaEntity>)

    @Query("SELECT * FROM medias WHERE is_liked = 1")
    fun getLiked(): List<MediaEntity>

    @Query("SELECT * FROM medias WHERE is_indicated = 1")
    fun getIndicated(): Flow<List<MediaEntity>>

    @Query("DELETE FROM medias WHERE is_liked = 0")
    fun deleteNotLiked()
}
