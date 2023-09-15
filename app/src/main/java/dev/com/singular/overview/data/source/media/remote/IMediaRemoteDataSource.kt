package dev.com.singular.overview.data.source.media.remote

import dev.com.singular.overview.data.model.filters.SearchFilters
import dev.com.singular.overview.data.model.media.Media
import dev.com.singular.overview.data.source.DataResult

interface IMediaRemoteDataSource<T : Media> {
    suspend fun find(apiId: Long): DataResult<T>

    suspend fun getPaging(page: Int, searchFilters: SearchFilters): List<T>

    suspend fun searchPaging(page: Int, searchFilters: SearchFilters): List<T>
}
