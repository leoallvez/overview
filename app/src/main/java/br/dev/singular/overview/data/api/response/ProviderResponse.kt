package br.dev.singular.overview.data.api.response

import br.dev.singular.overview.data.model.provider.Provider

data class ProviderResponse(
    val results: Map<String, Provider> = mapOf()
)
