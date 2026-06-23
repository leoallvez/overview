package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.source.responseToResult
import javax.inject.Inject

class TvShowRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource<TvShow> {

    override suspend fun find(apiId: Long) = _locale.run {
        val response = _api.getTvShow(id = apiId, language = language, region = region)
        responseToResult(response)
    }
}
