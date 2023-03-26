package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre_types")
class GenreType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "genre_type_db_id")
    val dbId: Long = 0,
    val key: String
)
