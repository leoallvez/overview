package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.api.ApiService
import br.dev.singular.overview.data.api.IApiLocale
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.TvShow
import br.dev.singular.overview.data.source.responseToResult
import br.dev.singular.overview.util.joinToStringWithPipe
import br.dev.singular.overview.util.toFormatted
import br.dev.singular.overview.util.toLastMonthFormatted
import com.haroldadmin.cnradapter.NetworkResponse
import java.util.Date
import javax.inject.Inject

class TvShowRemoteDataSource @Inject constructor(
    private val _api: ApiService,
    private val _locale: IApiLocale
) : IMediaRemoteDataSource<TvShow>, IMediaDiscoverRemoteDataSource<TvShow> {

    override suspend fun find(apiId: Long) = _locale.run {
        val response = _api.getTvShow(id = apiId, language = language, region = region)
        responseToResult(response)
    }

    override suspend fun getPaging(page: Int, searchFilters: SearchFilters): List<TvShow> {
        return when (val response = makePaging(page, searchFilters)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makePaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        val streamingId = searchFilters.streamingId.toString()
        val genresIds = searchFilters.getGenreIdsSeparatedWithComma()
        _api.getTvShowsPaging(streamingId, genresIds, page, language, region, region)
    }

    override suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<TvShow> {
        return when (val response = makeSearchPaging(page, searchFilters)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makeSearchPaging(page: Int, searchFilters: SearchFilters) = _locale.run {
        _api.searchTvShow(searchFilters.query, language, region, region, page)
    }

    override suspend fun discoverByStreamings(streamingsIds: List<Long>): List<TvShow> {
        return when (val response = makeTvDiscover(streamingsIds)) {
            is NetworkResponse.Success -> { response.body.results }
            else -> listOf()
        }
    }

    private suspend fun makeTvDiscover(streamingsIds: List<Long>) = _locale.run {
        val today: Date by lazy { Date() }
        _api.discoverOnTvByStreamings(
            language = language,
            watchRegion = region,
            dateIni = today.toLastMonthFormatted(),
            dateEnd = today.toFormatted(),
            streamingsIds = streamingsIds.joinToStringWithPipe()
        )
    }
}