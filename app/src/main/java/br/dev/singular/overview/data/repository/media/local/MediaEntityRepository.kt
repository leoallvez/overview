package br.dev.singular.overview.data.repository.media.local

import br.dev.singular.overview.data.model.media.MediaEntity
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityPagingRepository
import br.dev.singular.overview.data.repository.media.local.interfaces.IMediaEntityRepository
import br.dev.singular.overview.data.source.media.local.MediaLocalDataSource
import br.dev.singular.overview.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class MediaEntityRepository @Inject constructor(
    private val _source: MediaLocalDataSource,
    @IoDispatcher
    private val _dispatcher: CoroutineDispatcher,
) : IMediaEntityRepository, IMediaEntityPagingRepository {

    override suspend fun getLikedPaging() = withContext(_dispatcher) {
        _source.getAllLiked()
    }

    override suspend fun getLikedByTypePaging(type: String) = withContext(_dispatcher) {
        _source.getAllLikedByType(type)
    }

    override suspend fun update(media: MediaEntity) = withContext(_dispatcher) {
        _source.update(media)
    }

    override suspend fun deleteUnlikedOlderThan(date: Date) = withContext(_dispatcher) {
        _source.deleteUnlikedOlderThan(date)
    }

}
