package br.com.deepbyte.overview.data.source.media.remote

import br.com.deepbyte.overview.data.model.filters.SearchFilters
import br.com.deepbyte.overview.data.model.media.Media
import br.com.deepbyte.overview.data.source.DataResult

interface IMediaRemoteDataSource<T : Media> {
    suspend fun find(apiId: Long): DataResult<T>

    suspend fun getPaging(page: Int, searchFilters: SearchFilters): List<T>

    suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<T>
}
