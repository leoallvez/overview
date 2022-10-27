package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig

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
    @field:Json(name = "display_priority")
    val displayPriority: Int = 0,
    @field:Json(name = "logo_path")
    private val logoPath: String = "",
    @field:Json(name = "provider_name")
    val providerName: String = ""
) : DataResponse() {
    fun getLogoImage() = "${BuildConfig.IMG_URL}/$logoPath"
}
