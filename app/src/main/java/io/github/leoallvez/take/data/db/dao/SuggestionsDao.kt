package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.leoallvez.take.data.model.Suggestion
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionsDao {

    @Insert
    suspend fun insert(vararg suggestions: Suggestion)

    @Query("SELECT * FROM suggestions ORDER BY `order`")
    fun getAll(): Flow<List<Suggestion>>

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()

}
