package br.dev.singular.overview.domain.usecase.media

import br.dev.singular.overview.domain.model.Media
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.repository.GetAll
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState
import java.util.Calendar

interface IDeleteMediasCacheUseCase {
    suspend operator fun invoke(): UseCaseState<Boolean>
}

class DeleteMediasCacheUseCase(
    private val getter: GetAll<Media>,
    private val deleter: Delete<Media>,
    private val isTestMode: Boolean = false,
) : IDeleteMediasCacheUseCase {

    override suspend fun invoke(): UseCaseState<Boolean> {
        return runCatching {
            getMediasToDelete().also { deleter.delete(*it.toTypedArray()) }.isNotEmpty()
        }.fold(
            onSuccess = { wasDeleted -> UseCaseState.Success(wasDeleted) },
            onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
        )
    }

    private suspend fun getMediasToDelete(): List<Media> {
        return getter.getAll()
            .filterNot { it.isLiked }
            .filter { isTestMode || it.lastUpdate < getMaxDate() }
    }

    private fun getMaxDate() = Calendar.getInstance().run {
        add(Calendar.DAY_OF_MONTH, AMOUNT_DAYS_OF_CACHE)
        time
    }

    private companion object {
        const val AMOUNT_DAYS_OF_CACHE = -7
    }
}
