package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.deepbyte.overview.BuildConfig

@Entity(tableName = "media_suggestions")
class MediaSuggestion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_suggestion_db_id")
    var dbId: Long = 0,
    @ColumnInfo(name = "api_id")
    val apiId: Long = 0,
    val letter: String = "",
    val type: String = "",
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String = ""
) {
    fun getBackdropImage() = "${BuildConfig.IMG_URL}/$backdropPath"
}
