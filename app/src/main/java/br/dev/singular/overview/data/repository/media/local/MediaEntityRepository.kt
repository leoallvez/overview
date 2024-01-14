package br.dev.singular.overview.data.repository.media.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import br.dev.singular.overview.BuildConfig
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

    override fun getLikedPaging(type: MediaType): Pager<Int, MediaEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = BuildConfig.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { getLikedPagingSource(type) }
        )
    }

    private fun getLikedPagingSource(type: MediaType): PagingSource<Int, MediaEntity> {
        return if (type.key == MediaType.ALL.key) {
            _source.getAllLiked()
        } else {
            _source.getAllLikedByType(type.key)
        }
    }

    override suspend fun update(media: MediaEntity) = withContext(_dispatcher) {
        _source.update(media)
    }

    override suspend fun deleteUnlikedOlderThan(date: Date) = withContext(_dispatcher) {
        _source.deleteUnlikedOlderThan(date)
    }
}
