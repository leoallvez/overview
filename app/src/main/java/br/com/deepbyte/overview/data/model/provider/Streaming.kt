package br.com.deepbyte.overview.data.model.provider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.deepbyte.overview.BuildConfig
import com.squareup.moshi.Json

@Entity(tableName = "streamings")
class Streaming(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "streaming_db_id")
    @field:Json(name = "provider_id")
    val apiId: Long = 0,

    @ColumnInfo(name = "display_priority")
    @field:Json(name = "display_priority")
    val priority: Int = 0,

    @ColumnInfo(name = "logo_path")
    @field:Json(name = "logo_path")
    val logoPath: String = "",

    @ColumnInfo(name = "provider_name")
    @field:Json(name = "provider_name")
    val name: String = "",

    @ColumnInfo(name = "selected")
    @field:Json(name = "selected")
    val selected: Boolean = false
) {
    fun getLogoImage() = "${BuildConfig.IMG_URL}/$logoPath"
}
