package io.github.leoallvez.take.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import io.github.leoallvez.take.data.api.response.DiscoverResponse
import io.github.leoallvez.take.data.source.DataResult
import io.github.leoallvez.take.data.source.discover.DiscoverPagingSource
import io.github.leoallvez.take.data.source.discover.IDiscoverRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

typealias OnDiscover = suspend (Long, Int) -> DataResult<DiscoverResponse>

class DiscoverRepository @Inject constructor(
    private val _coroutineScope: CoroutineScope,
    private val _source: IDiscoverRemoteDataSource
) {

    fun discoverOnTvByProvider(providerId: Long, mediaType: String) =
        makeDiscoverPaging(
            apiId = providerId,
            mediaType = mediaType,
            onRequest = _source::discoverOnTvByProvider
        )

    fun discoverOnByGenre(providerId: Long, mediaType: String) =
        makeDiscoverPaging(
            apiId = providerId,
            mediaType = mediaType,
            onRequest = _source::discoverOnByGenre
        )

    private fun makeDiscoverPaging(apiId: Long, mediaType: String, onRequest: OnDiscover) =
        Pager(PagingConfig(pageSize = NETWORK_PAGE_SIZE)) {
            DiscoverPagingSource(apiId, mediaType, onRequest)
        }.flow.cachedIn(_coroutineScope)

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
