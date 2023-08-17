package br.com.deepbyte.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.deepbyte.overview.BuildConfig

@Entity(tableName = "medias")
class MediaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "media_db_id")
    var dbId: Long = 0,
    @ColumnInfo(name = "api_id")
    val apiId: Long = 0,
    val letter: String = "",
    val type: String = "",
    @ColumnInfo(name = "poster_path")
    val posterPath: String = "",
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String = "",
    @ColumnInfo(name = "is_liked")
    val isLiked: Boolean = false,
    @ColumnInfo(name = "is_indicated")
    val isIndicated: Boolean = false
) {
    fun getPosterImage() = "${BuildConfig.IMG_URL}/$posterPath"
    fun getBackdropImage() = "${BuildConfig.IMG_URL}/$backdropPath"
}
