package br.com.deepbyte.overview.data.source.streaming

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class StreamingRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IStreamingRemoteDataSource {

    override suspend fun getItems() = _locale.run {
        val response = _api.getStreamingItems(language = language, region = region)
        responseToResult(response)
    }
}
