package br.com.deepbyte.take.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.com.deepbyte.take.data.api.response.DiscoverResponse
import br.com.deepbyte.take.data.source.DataResult
import br.com.deepbyte.take.data.source.discover.DiscoverPagingSource
import br.com.deepbyte.take.data.source.discover.IDiscoverRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

typealias OnDiscover = suspend (page: Int) -> DataResult<DiscoverResponse>

class DiscoverRepository @Inject constructor(
    private val _coroutineScope: CoroutineScope,
    private val _source: IDiscoverRemoteDataSource
) {

    fun discoverOnTvByProvider(providerId: Long, mediaType: String) =
        makeDiscoverPaging(
            mediaType = mediaType,
            onRequest = { page: Int ->
                _source.discoverOnTvByProvider(providerId, page)
            }
        )

    fun discoverByGenre(genreId: Long, mediaType: String) =
        makeDiscoverPaging(
            mediaType = mediaType,
            onRequest = { page: Int ->
                _source.discoverByGenre(genreId, page, mediaType)
            }
        )

    private fun makeDiscoverPaging(mediaType: String, onRequest: OnDiscover) =
        Pager(PagingConfig(pageSize = NETWORK_PAGE_SIZE)) {
            DiscoverPagingSource(mediaType, onRequest)
        }.flow.cachedIn(_coroutineScope)

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
