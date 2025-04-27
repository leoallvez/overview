package br.dev.singular.overview.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "medias")
class MediaDataModel(
    @PrimaryKey
    @field:Json(name = "id")
    var id: Long = 0,
    private val name: String? = "",
    private val title: String? = "",
    @field:Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String? = "",
    @field:Json(name = "media_type")
    @ColumnInfo(name = "media_type")
    var type: String? = ""
) {
    val betterTitle: String
        get() = when {
            name.isNullOrBlank().not() -> name
            title.isNullOrBlank().not() -> title
            else -> ""
        }
}
