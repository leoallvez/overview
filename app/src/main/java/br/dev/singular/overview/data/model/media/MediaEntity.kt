package br.dev.singular.overview.data.model.media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.dev.singular.overview.BuildConfig

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
    val posterPath: String? = null,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String? = null,
    @ColumnInfo(name = "is_liked")
    var isLiked: Boolean = false,
    @ColumnInfo(name = "is_indicated")
    var isIndicated: Boolean = false
) {
    fun getBackdropImage() = "${BuildConfig.IMG_URL}/$backdropPath"
}