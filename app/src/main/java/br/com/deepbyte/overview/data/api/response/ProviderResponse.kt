package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.data.model.provider.Provider
import com.squareup.moshi.Json

data class ProviderResponse(
    @field:Json(name = "id")
    val apiId: Long = 0,
    val results: Map<String, Provider> = mapOf()
)
