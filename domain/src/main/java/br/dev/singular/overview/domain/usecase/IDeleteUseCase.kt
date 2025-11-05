package br.dev.singular.overview.domain.usecase

interface IDeleteUseCase {
    suspend operator fun invoke(): UseCaseState<Boolean>
}
