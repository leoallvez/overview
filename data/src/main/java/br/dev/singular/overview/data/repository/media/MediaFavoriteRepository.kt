package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IPendingFavoriteLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.network.source.IFavoriteRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Observe
import br.dev.singular.overview.domain.repository.Update
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

class MediaFavoriteRepository @Inject constructor(
    private val authDataSource: IAuthRemoteDataSource,
    private val remoteDataSource: IFavoriteRemoteDataSource,
    private val pendingDataSource: IPendingFavoriteLocalDataSource
) : Update<Media>, Observe<List<Media>> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<List<Media>> {
        return authDataSource.observe()
            .map { it?.id }
            .distinctUntilChanged()
            .flatMapLatest { userId ->
                if (userId == null) flowOf(emptyList()) else observeFavorites(userId)
            }
    }

    override suspend fun update(item: Media) {
        val userId = currentUserId()
        val model = item.toData()

        if (model.isLiked) {
            remoteDataSource.save(userId, listOf(model.copy(lastUpdate = Date())))
        } else {
            remoteDataSource.delete(userId, model)
        }
    }

    private fun observeFavorites(userId: String): Flow<List<Media>> {
        return remoteDataSource.observe(userId)
            .map { models -> models.map { it.toDomain() } }
            .catch {
                Timber.e(it)
                emit(emptyList())
            }
            .onStart { syncPending(userId) }
    }

    private suspend fun syncPending(userId: String) {
        runCatching {
            val pending = pendingDataSource.getAll()
            if (pending.isNotEmpty()) {
                remoteDataSource.saveAll(userId, pending)
                pendingDataSource.clear()
            }
        }.onFailure { Timber.e(it) }
    }

    private fun currentUserId(): String {
        return checkNotNull(authDataSource.currentUser()) { "Authenticated user not found" }.id
    }
}
