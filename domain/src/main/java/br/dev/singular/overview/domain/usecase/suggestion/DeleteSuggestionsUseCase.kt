package br.dev.singular.overview.domain.usecase.suggestion

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.DeleteAll
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IDeleteSuggestionsUseCase {
    suspend operator fun invoke(): UseCaseState<Unit>
}

class DeleteSuggestionsUseCase(
    private val deleter: DeleteAll<Suggestion>
) : IDeleteSuggestionsUseCase {

    override suspend fun invoke(): UseCaseState<Unit> {
        return runCatching { deleter.deleteAll() }
            .fold(
                onSuccess = { UseCaseState.Success(Unit) },
                onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
            )
    }
}
