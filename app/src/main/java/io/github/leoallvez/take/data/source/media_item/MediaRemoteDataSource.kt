package io.github.leoallvez.take.data.source.media_item

import io.github.leoallvez.take.data.api.ApiService
import io.github.leoallvez.take.data.api.IApiLocale
import io.github.leoallvez.take.data.api.response.ListContentResponse
import io.github.leoallvez.take.data.api.response.MediaDetailResponse
import io.github.leoallvez.take.data.api.response.ProviderResponse
import io.github.leoallvez.take.data.model.MediaItem
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.parserResponseToResult
import javax.inject.Inject

interface IMediaRemoteDataSource {

    suspend fun getMediaItems(url: String): DataResult<ListContentResponse<MediaItem>>

    suspend fun getProvidersResult(id: Long, type: String): DataResult<ProviderResponse>

    suspend fun getMediaDetailsResult(id: Long, type: String): DataResult<MediaDetailResponse>
}

class MediaRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource {

    override suspend fun getMediaItems(url: String) = parserResponseToResult(
        _api.getMediaItems(url = url, language = _locale.language, region = _locale.region)
    )

    override suspend fun getProvidersResult(id: Long, type: String) = parserResponseToResult(
        _api.getProviders(
            id = id, type = type, language = _locale.language, region = _locale.region
        )
    )

    override suspend fun getMediaDetailsResult(id: Long, type: String) = parserResponseToResult(
        _api.getMediaDetail(
            id = id, type = type, language = _locale.language, region = _locale.region
        )
    )

}
