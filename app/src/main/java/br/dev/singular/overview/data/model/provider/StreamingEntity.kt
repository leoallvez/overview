package br.dev.singular.overview.data.model.provider

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import br.dev.singular.overview.BuildConfig
import com.squareup.moshi.Json
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "streamings")
data class StreamingEntity(
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
    var selected: Boolean = false
) : Parcelable {

    @Ignore
    @IgnoredOnParcel
    @field:Json(name = "display_priorities")
    val displayPriorities: Map<String, Int> = mapOf()

    fun getLogoImage() = "${BuildConfig.TMDB_IMG_URL}/$logoPath"
}
