package br.com.deepbyte.overview.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.deepbyte.overview.data.model.media.MediaSuggestion

@Dao
interface MediaSuggestionDao {

    @Insert
    fun insert(vararg mediaSuggestion: MediaSuggestion)

    @Query("SELECT * FROM media_suggestions")
    fun getAll(): List<MediaSuggestion>

    @Query("DELETE FROM media_suggestions")
    fun deleteAll()
}
