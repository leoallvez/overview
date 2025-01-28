package br.dev.singular.overview.data.source.trailer

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.model.media.Trailer
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class TrailerRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : ITrailerRemoteDataSource {

    override suspend fun getAll(type: String, apiId: Long): List<Trailer> {
        return when (val response = getTrailers(apiId, type)) {
            is NetworkResponse.Success -> response.body.results
            else -> emptyList()
        }
    }

    private suspend fun getTrailers(apiId: Long, type: String) = _locale.run {
        _api.getTrailers(id = apiId, mediaType = type, language = language)
    }
}
