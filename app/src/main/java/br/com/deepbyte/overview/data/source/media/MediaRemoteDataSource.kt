package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource {

    override suspend fun getItems(url: String) =  responseToResult(
        _api.getMediaItems(url = url, language = _locale.language, region = _locale.region)
    )

    override suspend fun getItem(id: Long, type: String) = responseToResult(
        _api.getMediaDetail(id = id, type = type, language = _locale.language, region = _locale.region)
    )

    override suspend fun search(mediaType: String, query: String) = _locale.run {
        responseToResult(_api.searchMedia(mediaType, query, language, region, region))
    }
}
