package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.model.User
import br.dev.singular.overview.domain.repository.Get
import br.dev.singular.overview.domain.repository.Update
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import br.dev.singular.overview.domain.usecase.runSafely

interface IToggleFavoriteUseCase {
    suspend operator fun invoke(media: Media): UseCaseState<Boolean>
}

class ToggleFavoriteUseCase(
    private val session: Get<User?>,
    private val updater: Update<Media>
) : IToggleFavoriteUseCase {

    override suspend fun invoke(media: Media): UseCaseState<Boolean> {
        val user = runCatching { session.get() }.getOrNull()
        if (user == null) return UseCaseState.Failure(FailType.Unauthorized)

        return runSafely {
            val isLiked = !media.isLiked
            updater.update(media.copy(isLiked = isLiked))
            isLiked
        }
    }
}
