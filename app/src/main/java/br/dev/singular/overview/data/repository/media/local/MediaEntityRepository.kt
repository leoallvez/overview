package br.dev.singular.overview.data.repository.media.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import br.dev.singular.overview.data.model.filters.SearchFilters
import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityPagingRepository
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityRepository
import br.dev.singular.overview.data.source.media.MediaType
import br.dev.singular.overview.data.source.media.local.MediaLocalDataSource
import br.dev.singular.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class MediaEntityRepository @Inject constructor(
    private val _source: MediaLocalDataSource,
    @IoDispatcher
    private val _dispatcher: CoroutineDispatcher
) : IMediaEntityRepository, IMediaEntityPagingRepository {

    override fun getLikedPaging(filters: SearchFilters): Pager<Int, MediaEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { getLikedPagingSource(filters) }
        )
    }

    private fun getLikedPagingSource(searchFilters: SearchFilters): PagingSource<Int, MediaEntity> {
        val type = searchFilters.mediaType.key
        return if (type == MediaType.ALL.key) {
            _source.getAllLiked()
        } else {
            _source.getAllLikedByType(type)
        }
    }

    override suspend fun update(media: MediaEntity) = withContext(_dispatcher) {
        _source.update(media)
    }

    override suspend fun deleteUnlikedOlderThan(date: Date) = withContext(_dispatcher) {
        _source.deleteUnlikedOlderThan(date)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
