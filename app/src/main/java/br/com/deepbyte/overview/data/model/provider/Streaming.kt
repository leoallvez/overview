package br.com.deepbyte.overview.data.model.provider

import br.com.deepbyte.overview.BuildConfig
import com.squareup.moshi.Json

class Streaming(
    @field:Json(name = "provider_id")
    val apiId: Long = 0,
    @field:Json(name = "display_priority")
    val priority: Int = 0,
    @field:Json(name = "logo_path")
    private val logoPath: String = "",
    @field:Json(name = "provider_name")
    val name: String = ""
) {
    fun getLogoImage() = "${BuildConfig.IMG_URL}/$logoPath"
}
