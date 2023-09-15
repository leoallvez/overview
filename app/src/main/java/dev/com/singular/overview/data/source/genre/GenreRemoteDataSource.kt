package dev.com.singular.overview.data.source.genre

import dev.com.singular.overview.data.api.ApiService
import dev.com.singular.overview.data.api.IApiLocale
import dev.com.singular.overview.data.source.media.MediaTypeEnum
import dev.com.singular.overview.data.source.responseToResult
import javax.inject.Inject

class GenreRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IGenreRemoteDataSource {

    override suspend fun getItemByMediaType(type: MediaTypeEnum) = _locale.run {
        val response = _api.getGenreByMediaType(type.key, language = language, region = region)
        responseToResult(response)
    }
}
