package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource {

    override suspend fun getItems(url: String) = _locale.run {
        val response = _api.getMediaItems(url = url, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun getItem(id: Long, type: String) = _locale.run {
        val response = _api
            .getMediaItem(id = id, type = type, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun search(mediaType: String, query: String) = _locale.run {
        val response = _api.searchMedia(mediaType, query, language, region, region)
        responseToResult(response)
    }
}
