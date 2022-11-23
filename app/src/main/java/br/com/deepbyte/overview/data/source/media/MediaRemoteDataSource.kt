package br.com.deepbyte.overview.data.source.media

import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.api.response.ListContentResponse
import br.com.deepbyte.overview.data.api.response.MediaDetailResponse
import br.com.deepbyte.overview.data.model.MediaItem
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

interface IMediaRemoteDataSource {
    suspend fun getMediaItemsResult(url: String): DataResult<ListContentResponse<MediaItem>>
    suspend fun getMediaDetailsResult(id: Long, type: String): DataResult<MediaDetailResponse>
}

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource {

    override suspend fun getMediaItemsResult(url: String) = responseToResult(
        _api.getMediaItems(url = url, language = _locale.language, region = _locale.region)
    )

    override suspend fun getMediaDetailsResult(id: Long, type: String) = responseToResult(
        _api.getMediaDetail(
            id = id, type = type, language = _locale.language, region = _locale.region
        )
    )
}
