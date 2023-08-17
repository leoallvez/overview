package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import br.com.deepbyte.overview.data.model.media.MediaEntity

@Dao
interface MediaDao {

    @Insert(onConflict = REPLACE)
    fun insert(models: List<MediaEntity>)

    @Query("SELECT * FROM medias WHERE is_liked = 1")
    fun getLiked(): List<MediaEntity>

    @Query("SELECT * FROM medias WHERE is_indicated = 1")
    fun getIndicated(): List<MediaEntity>

    // TODO: move this logic to repository
    @Transaction
    fun update(newMedias: List<MediaEntity>) {
        val likedMedias = getLiked()
        resetIndicated()
        deleteNotLiked()

        newMedias.forEach { newMedia ->
            with(newMedia) {
                val likedMedia = likedMedias.find { it.apiId == newMedia.apiId }
                likedMedia?.let {
                    isLiked = true
                    dbId = likedMedia.dbId
                }
                isIndicated = true
            }
        }

        insert(newMedias)
    }

    @Query("DELETE FROM medias WHERE is_liked = 0")
    fun deleteNotLiked()

    @Query("UPDATE medias SET is_indicated = 0")
    fun resetIndicated()
}
