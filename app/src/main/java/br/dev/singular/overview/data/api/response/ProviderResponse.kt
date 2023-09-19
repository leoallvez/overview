package br.dev.singular.overview.data.api.response

import com.squareup.moshi.Json
import br.dev.singular.overview.data.model.provider.Provider

data class ProviderResponse(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val results: Map<String, Provider> = mapOf()
)
