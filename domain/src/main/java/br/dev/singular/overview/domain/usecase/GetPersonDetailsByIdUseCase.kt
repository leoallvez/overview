package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.PersonDetails
import br.dev.singular.overview.domain.repository.GetById

interface IGetPersonDetailsByIdUseCase {
    suspend operator fun invoke(id: Long): UseCaseState<PersonDetails?>
}

class GetPersonDetailsByIdUseCase(
    private val getter: GetById<PersonDetails>
) : IGetPersonDetailsByIdUseCase {
    override suspend fun invoke(id: Long) = runSafely { getter.getById(id) }
}
