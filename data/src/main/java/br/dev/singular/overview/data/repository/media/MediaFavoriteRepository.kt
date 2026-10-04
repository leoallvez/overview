package br.dev.singular.overview.data.repository.media

import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.network.source.IFavoriteRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.data.util.mappers.domainToData.toData
import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.Update
import javax.inject.Inject

class MediaFavoriteRepository @Inject constructor(
    private val authDataSource: IAuthRemoteDataSource,
    private val remoteDataSource: IFavoriteRemoteDataSource,
    private val localDataSource: IMediaLocalDataSource
) : Update<Media>, GetAll<Media> {

    override suspend fun getAll(): List<Media> {
        return remoteDataSource.getAll(currentUserId()).map { it.toDomain() }
    }

    override suspend fun update(item: Media) {
        val userId = currentUserId()
        val model = item.toData()

        if (model.isLiked) {
            remoteDataSource.save(userId, listOf(model))
        } else {
            remoteDataSource.delete(userId, model)
        }
        localDataSource.update(model)
    }

    private fun currentUserId(): String {
        return checkNotNull(authDataSource.currentUser()) { "Authenticated user not found" }.id
    }
}
