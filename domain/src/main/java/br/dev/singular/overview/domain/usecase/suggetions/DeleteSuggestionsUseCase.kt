package br.dev.singular.overview.domain.usecase.suggetions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Delete
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface IDeleteSuggestionsUseCase {
    suspend operator fun invoke(vararg suggestions: Suggestion): UseCaseState<Unit>
}

class DeleteSuggestionsUseCase(
    private val suggestionDeleter: Delete<Suggestion>
) : IDeleteSuggestionsUseCase {

    override suspend fun invoke(vararg suggestions: Suggestion): UseCaseState<Unit> {
        return runCatching { suggestionDeleter.delete(*suggestions) }
            .fold(
                onSuccess = { UseCaseState.Success(Unit) },
                onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
            )
    }
}