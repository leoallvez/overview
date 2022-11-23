package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.BuildConfig
import com.squareup.moshi.Json

data class ProviderResponse(
    val results: Map<String, Provider> = mapOf()
) : DataResponse()

data class Provider(
    private val link: String,
    @field:Json(name = "flatrate")
    private val flatRate: List<ProviderPlace>?
) {
    fun getOrderedFlatRate(): List<ProviderPlace> {
        return flatRate?.sortedBy { it.displayPriority } ?: listOf()
    }
}

class ProviderPlace(
    @field:Json(name = "provider_id")
    val apiId: Long = 0,
    @field:Json(name = "display_priority")
    val displayPriority: Int = 0,
    @field:Json(name = "logo_path")
    private val logoPath: String = "",
    @field:Json(name = "provider_name")
    val providerName: String = ""
) {
    fun getLogoImage() = "${BuildConfig.IMG_URL}/$logoPath"
}
