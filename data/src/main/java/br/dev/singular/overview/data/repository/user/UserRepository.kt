package br.dev.singular.overview.data.repository.user

import androidx.datastore.preferences.core.stringPreferencesKey
import br.dev.singular.overview.data.local.source.DataStoreDataSource
import br.dev.singular.overview.data.local.source.IMediaLocalDataSource
import br.dev.singular.overview.data.network.source.IAuthRemoteDataSource
import br.dev.singular.overview.data.network.source.IFavoriteRemoteDataSource
import br.dev.singular.overview.data.util.mappers.dataToDomain.toDomain
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Clear
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.GetByParam
import br.dev.singular.overview.domain.repository.Observe
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dataSource: IAuthRemoteDataSource,
    private val favoriteDataSource: IFavoriteRemoteDataSource,
    private val mediaDataSource: IMediaLocalDataSource,
    private val dataStoreDataSource: DataStoreDataSource
) : GetByParam<User, String>, Get<User?>, Observe<User?>, Clear {

    override fun observe(): Flow<User?> = dataSource.observe().map { it?.toDomain() }

    override suspend fun get(): User? = dataSource.currentUser()?.toDomain()

    override suspend fun getByParam(param: String): User {
        val user = dataSource.signIn(param)
        claimLocalFavorites(user.id)
        return user.toDomain()
    }

    override suspend fun clear() {
        dataSource.signOut()
        mediaDataSource.clearLiked()
    }

    // Likes without an owner were made before signing in and only exist on this device, so
    // they are uploaded. The write is queued by Firestore and does not need the network now.
    // Likes left by another account, whose session ended without a sign out, are discarded.
    // A failure must not undo a sign in that has already succeeded.
    private suspend fun claimLocalFavorites(userId: String) {
        try {
            val ownerId = dataStoreDataSource.getValue(FAVORITES_OWNER).first()
            if (ownerId == null) {
                favoriteDataSource.save(userId, mediaDataSource.getAll().filter { it.isLiked })
            } else if (ownerId != userId) {
                mediaDataSource.clearLiked()
            }
            dataStoreDataSource.setValue(FAVORITES_OWNER, userId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private companion object {
        val FAVORITES_OWNER = stringPreferencesKey(name = "favorites_owner_id")
    }
}
