package io.github.leoallvez.take.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.leoallvez.take.data.model.TvShow
import io.github.leoallvez.take.util.removeRepeated

@Dao
interface TvShowDao {

    @Transaction
    suspend fun update(vararg models: TvShow) {
        val result = models
            .removeRepeated(listToCompare = getAllAsList())
            .toTypedArray()

        insert(*result)
    }

    @Insert suspend fun insert(vararg model: TvShow)

    @Query("SELECT * FROM tv_shows order by tv_show_id desc;")
    fun getAllAsList(): List<TvShow>
}