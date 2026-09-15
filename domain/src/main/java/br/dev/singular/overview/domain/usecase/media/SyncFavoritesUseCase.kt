package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface ISyncFavoritesUseCase {
    /** Returns whether the local favorites were changed by the sync. */
    suspend operator fun invoke(): UseCaseState<Boolean>
}

class SyncFavoritesUseCase(
    private val session: Get<User?>,
    private val remote: GetAll<Media>,
    private val local: GetAll<Media>,
    private val saver: Update<List<Media>>
) : ISyncFavoritesUseCase {

    override suspend fun invoke(): UseCaseState<Boolean> {
        val user = runCatching { session.get() }.getOrNull()
        if (user == null) return UseCaseState.Success(false)

        return runSafely {
            val remoteFavorites = remote.getAll()
            val localFavorites = local.getAll()

            val remoteKeys = remoteFavorites.map { it.key }.toSet()
            val localKeys = localFavorites.map { it.key }.toSet()

            // The remote favorites are the source of truth, as they are shared across devices.
            val removed = localFavorites
                .filterNot { it.key in remoteKeys }
                .map { it.copy(isLiked = false) }
            val added = remoteFavorites
                .filterNot { it.key in localKeys }
                .map { it.copy(isLiked = true) }

            val changes = removed + added
            if (changes.isNotEmpty()) saver.update(changes)
            changes.isNotEmpty()
        }
    }

    private val Media.key get() = type to id
}
