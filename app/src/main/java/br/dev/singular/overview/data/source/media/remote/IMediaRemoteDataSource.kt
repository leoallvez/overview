package br.dev.singular.overview.data.source.media.remote

import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.Media
import br.dev.singular.overview.data.source.DataResult

interface IMediaRemoteDataSource<T : Media> {
    suspend fun find(apiId: Long): DataResult<T>

    suspend fun getPaging(page: Int, searchFilters: SearchFilters): List<T>

    suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<T>
}
