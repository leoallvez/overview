package br.com.deepbyte.overview.data.source.media.v1

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

// TODO: refactor - this will be deleted
class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource {

    override suspend fun getItems(url: String) = _locale.run {
        val response = _api.getMediaItems(url = url, language = language, region = region)
        responseToResult(response)
    }
}
