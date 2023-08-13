package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_suggestions")
data class MediaSuggestion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_suggestion_db_id")
    var dbId: Long = 0,
    @ColumnInfo(name = "api_id")
    val apiId: Long = 0,
    val letter: String = "",
    val type: String = "",
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String = ""
)
// test