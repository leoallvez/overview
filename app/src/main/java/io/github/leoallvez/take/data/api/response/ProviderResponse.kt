package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json
import io.github.leoallvez.take.BuildConfig

data class ProviderResponse (
    private val id: Long,
    val results: Map<String, Provider>
)

data class Provider (
    private val link: String,
    @field:Json(name = "flatrate")
    private val flatRate: List<ProviderPlace>?
) {
    fun getOrderedFlatRate(): List<ProviderPlace> {
        return flatRate?.sortedBy { it.displayPriority } ?: listOf()
    }
}

class ProviderPlace(
    val id: Long = 0L,
    @field:Json(name = "display_priority")
    val displayPriority: Int = 0,
    @field:Json(name = "logo_path")
    private val logoPath: String = "",
    @field:Json(name = "provider_name")
    val providerName: String = "",
) {
    fun getLogoImage() = "${BuildConfig.IMG_URL}/$logoPath"
}
