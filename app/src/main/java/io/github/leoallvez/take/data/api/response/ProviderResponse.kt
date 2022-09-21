package io.github.leoallvez.take.data.api.response

import com.squareup.moshi.Json

data class ProviderResponse (
    val id: Long,
    val
    results: Map<String, Provider>
)

data class Provider (
    val link: String,
    @field:Json(name = "flatrate")
    val flatRate: List<ProviderPlace>
)

class ProviderPlace(
    @field:Json(name = "display_priority")
    val displayPriority: Int = 0,
    @field:Json(name = "provider_id")
    val providerId: Long = 0,
    @field:Json(name = "provider_name")
    val providerName: String = "",
)