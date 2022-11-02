package io.github.leoallvez.take.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import io.github.leoallvez.take.data.source.discover.DiscoverPagingSource
import io.github.leoallvez.take.data.source.discover.IDiscoverRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DiscoverRepository @Inject constructor(
    private val _source: IDiscoverRemoteDataSource
) {

    fun load(providerId: Long, scope: CoroutineScope) =
        Pager(PagingConfig(pageSize = NETWORK_PAGE_SIZE)) {
            DiscoverPagingSource(providerId, _source)
        }.flow.cachedIn(scope)

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
