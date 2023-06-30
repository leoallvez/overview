package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "genre_db_id")
    var dbId: Long = 0,
    @field:Json(name = "id")
    @ColumnInfo(name = "api_id")
    val apiId: Long = 0,
    val name: String = ""
)
