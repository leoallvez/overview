package br.com.deepbyte.overview.data.api.response

import br.com.deepbyte.overview.data.model.provider.Provider

data class ProviderResponse(
    val results: Map<String, Provider> = mapOf()
) : DataResponse()
