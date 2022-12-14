package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : MediaItemRemoteDataSource {

    override suspend fun getItems(url: String) = _locale.run {
        val response = _api.getMediaItems(url = url, language = language, region = region)
        responseToResult(response)
    }
}
