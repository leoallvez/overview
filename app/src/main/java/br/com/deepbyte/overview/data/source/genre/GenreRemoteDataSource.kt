package br.com.deepbyte.overview.data.source.genre

import br.com.deepbyte.overview.data.source.media.MediaTypeEnum
import br.com.deepbyte.overview.data.api.ApiService
import br.com.deepbyte.overview.data.api.IApiLocale
import br.com.deepbyte.overview.data.source.responseToResult
import javax.inject.Inject

class GenreRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IGenreRemoteDataSource {

    override suspend fun getItemByMediaType(mediaType: MediaTypeEnum) = _locale.run {
        val response = _api.getGenreByMediaType(mediaType.key, language = language, region = region)
        responseToResult(response)
    }
}
