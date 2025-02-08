package br.dev.singular.overview.data.source.video

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.model.media.Video
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class VideoRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IVideoRemoteDataSource {

    override suspend fun getItems(apiId: Long, type: String): List<Video> {
        return when (val response = getVideos(apiId, type)) {
            is NetworkResponse.Success -> response.body.results
            else -> emptyList()
        }
    }

    private suspend fun getVideos(apiId: Long, type: String) = _locale.run {
        _api.getVideos(id = apiId, mediaType = type, language = language)
    }
}
