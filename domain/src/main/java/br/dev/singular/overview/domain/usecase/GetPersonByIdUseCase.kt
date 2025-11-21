package br.dev.singular.overview.domain.usecase

import br.dev.singular.overview.domain.model.Person
import br.dev.singular.overview.domain.repository.GetById

interface IGetPersonByIdUseCase {
    suspend operator fun invoke(id: Long): UseCaseState<Person?>
}

class GetPersonByIdUseCase(
    private val getter: GetById<Person>
) : IGetPersonByIdUseCase {
    override suspend fun invoke(id: Long) = runSafely { getter.getById(id) }
}
