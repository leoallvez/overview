package io.github.leoallvez.take.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.github.leoallvez.take.data.model.Suggestions
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionsDao {

    @Insert
    suspend fun insert(vararg suggestions: Suggestions)

//    @Query("SELECT * FROM suggestions ORDER BY `order`")
//    suspend fun getAll(): Flow<List<Suggestions>>

    @Query("DELETE FROM suggestions")
    suspend fun deleteAll()
}