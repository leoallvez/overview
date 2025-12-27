package br.dev.singular.overview.data.model.provider

import android.os.Parcelable
import br.dev.singular.overview.presentation.BuildConfig
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamingEntity(
    @field:Json(name = "provider_id")
    val apiId: Long = 0,
    @field:Json(name = "display_priority")
    val priority: Int = 0,
    @field:Json(name = "logo_path")
    val logoPath: String = "",
    @field:Json(name = "provider_name")
    val name: String = "",
    var selected: Boolean = false
) : Parcelable {
    fun getLogoImage() = "${BuildConfig.IMG_URL}/$logoPath"
}
