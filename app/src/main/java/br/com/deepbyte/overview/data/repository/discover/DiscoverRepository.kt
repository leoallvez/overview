package br.com.deepbyte.overview.data.repository.discover

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.com.deepbyte.overview.data.api.response.DiscoverResponse
import br.com.deepbyte.overview.data.source.DataResult
import br.com.deepbyte.overview.data.source.discover.DiscoverPagingSource
import br.com.deepbyte.overview.data.source.discover.IDiscoverRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

typealias OnDiscover = suspend (page: Int) -> DataResult<DiscoverResponse>

class DiscoverRepository @Inject constructor(
    private val _coroutineScope: CoroutineScope,
    private val _source: IDiscoverRemoteDataSource
) : IDiscoverRepository {

    override fun discoverByProvideId(providerId: Long, mediaType: String) =
        makeDiscoverPaging(
            mediaType = mediaType,
            onRequest = { page: Int ->
                _source.discoverByProviderId(providerId, page)
            }
        )

    override fun discoverByGenreId(genreId: Long, mediaType: String) =
        makeDiscoverPaging(
            mediaType = mediaType,
            onRequest = { page: Int ->
                _source.discoverByGenreId(genreId, page, mediaType)
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
