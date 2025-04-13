package br.dev.singular.overview.domain.usecase.suggetions

import br.dev.singular.overview.domain.model.Suggestion
import br.dev.singular.overview.domain.repository.Create
import br.dev.singular.overview.domain.usecase.FailType
import br.dev.singular.overview.domain.usecase.UseCaseState

interface ICreateSuggestionsUseCase {
    suspend operator fun invoke(vararg suggestions: Suggestion): UseCaseState<Unit>
}

class CreateSuggestionsUseCase(
    private val creator: Create<Suggestion>
) : ICreateSuggestionsUseCase {

    override suspend fun invoke(vararg suggestions: Suggestion): UseCaseState<Unit> {
        return runCatching { creator.create(*suggestions) }
            .fold(
                onSuccess = { UseCaseState.Success(Unit) },
                onFailure = { UseCaseState.Failure(FailType.Exception(it)) }
            )
    }
}
